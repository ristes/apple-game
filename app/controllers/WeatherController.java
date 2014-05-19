package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dto.DayWeatherDao;

import models.Day;
import models.Farmer;
import models.WeatherType;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class WeatherController extends Controller{
	public static void weatherforecast( String fordays) throws IOException{
		List<DayWeatherDao> result = new ArrayList<DayWeatherDao>();
		Day myday = null;
		Integer fordaysInt = Integer.parseInt(fordays);
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			JsonController.toJson(result);
		}
		List<Day> day = Day.find("byDayOrder", farmer.gameDate.dayOrder).fetch();
		if (day.size()>0) {
			myday = day.get(0);
		}
		String[] listDayOrders = new String[fordaysInt];
		for (int i=0;i<fordaysInt;i++) {
			listDayOrders[i]=String.valueOf(myday.dayOrder+i+1);
		}
		String whereClause = StringUtils.join(listDayOrders,",");
		String sql = new String(String.format("SELECT * FROM Day WHERE dayOrder IN (%s)",whereClause));
		List<Object[]> resultForecast = JPA.em().createNativeQuery(sql).getResultList();
		for (Object[] obj: resultForecast) {
			Long weatherTypeLong = ((BigInteger)obj[8]).longValue();
			Date date = (Date)obj[9];
			Double lowTemp = ((Double)obj[6]).doubleValue();
			Double highTemp = ((Double)obj[5]).doubleValue();
			DayWeatherDao weather = new DayWeatherDao();
			weather.date = date;
			weather.highTemp = highTemp;
			weather.lowTemp = lowTemp;
			WeatherType wType = WeatherType.findById(weatherTypeLong);
			weather.weatherType = wType.id;
			weather.icon_url = wType.icon.name;;
//			weather.icon_name = wType.icon.name;
			result.add(weather);
		}
		JsonController.toJson(result);
		
	}
	
	

}
