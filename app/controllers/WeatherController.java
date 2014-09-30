package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import dto.DayWeatherDao;
import models.Day;
import models.Farmer;
import models.WeatherType;
import play.db.jpa.JPA;
import play.mvc.Controller;
import service.ContextService;
import service.impl.ContextServiceImpl;

public class WeatherController extends Controller {

	public static void weatherforecast(String fordays) throws IOException {
		List<DayWeatherDao> result = new ArrayList<DayWeatherDao>();

		Day myday = null;
		Integer fordaysInt = Integer.parseInt(fordays);
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			JsonController.toJson(result);
		}
		List<Day> day = Day.find("byDayOrder", farmer.gameDate.dayOrder)
				.fetch();
		if (day.size() > 0) {
			myday = day.get(0);
		}
		List<String> listDayOrders = new ArrayList<String>();

		for (int i = 0; i < fordaysInt; i++) {
			long dayOrder = myday.dayOrder - fordaysInt / 2 + i;
			if (dayOrder >= 0) {
				listDayOrders.add(String.valueOf(dayOrder));
			}
		}
		String whereClause = StringUtils.join(listDayOrders, ",");
		String sql = new String(String.format(
				"SELECT * FROM day WHERE dayOrder IN (%s)", whereClause));
		List<Object[]> resultForecast = JPA.em().createNativeQuery(sql)
				.getResultList();
		for (Object[] obj : resultForecast) {
			Long weatherTypeLong = ((BigInteger) obj[8]).longValue();
			Long dayOrder = ((BigInteger) obj[10]).longValue();
			Date date = (Date) obj[9];

			Double lowTemp = ((Double) obj[6]).doubleValue();
			Double highTemp = ((Double) obj[5]).doubleValue();
			DayWeatherDao weather = new DayWeatherDao();
			weather.date = date;
			weather.highTemp = highTemp;
			weather.lowTemp = lowTemp;
			WeatherType wType = WeatherType.findById(weatherTypeLong);
			weather.weatherType = wType.id;
			weather.icon_url = wType.icon.name;
			weather.uvProb = ((Double) obj[7]).doubleValue();
			weather.humidity = ((Integer) obj[2]).intValue();
			ContextService contextS = new ContextServiceImpl();
			if (weather.weatherType==WeatherType.WEATHER_TYPE_RAINY || weather.weatherType==WeatherType.WEATHER_TYPE_ICY) {
				weather.rainQuantity = contextS.rainCoefForMonth(weather.date);
			} else {
				weather.rainQuantity = 0.0;
			}
			weather.id = ((BigInteger) obj[10]).intValue();
			if (dayOrder < farmer.gameDate.dayOrder) {
				weather.type = "past-future";
			}
			if (dayOrder.longValue() == farmer.gameDate.dayOrder) {
				weather.type = "today";
			}
			if (dayOrder > farmer.gameDate.dayOrder) {
				weather.type = "past-future";
			}

			result.add(weather);
		}
		JsonController.toJson(result);

	}

	

	
	
}
