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
				"SELECT * FROM Day WHERE dayOrder IN (%s)", whereClause));
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

	/**
	 * describes the weather season depending of gamedate of the farmer state 
	 * 2. Autumn - 01.10 - 30.11 
	 * 1. Winter - 01.12 - 28.02 
	 * 3. Spring - 01.03 - 30.04 
	 * 4. Summer - 01.05 - 30.09
	 * 
	 * @param farmer
	 */
	public static int season_level(Farmer farmer) {
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
		case 0:
			return 1;
		case 1:
			return 1;
		case 2:
			return 3;
		case 3:
			return 3;
		case 4:
			return 4;
		case 5:
			return 4;
		case 6:
			return 4;
		case 7:
			return 4;
		case 8:
			return 4;
		case 9:
			return 2;
		case 10:
			return 2;
		case 11:
			return 1;
		}
		return -1;
	}

	public static int seasionLevelSoilImage(int season_level) {
		if (season_level == 3 || season_level == 4) {
			return 1;
		}
		return season_level;
	}

	public static int evaluateYearLevel(Integer year) {
		int year_level = 0;
		switch (year) {
		case 2020:
			year_level = 1;
			break;
		case 2021:
			year_level = 2;
			break;
		default:
			year_level = 3;
		}
		return year_level;
	}
	
	
}
