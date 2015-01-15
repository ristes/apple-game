package controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.Day;
import models.Farmer;
import models.WeatherType;

import org.apache.commons.lang.StringUtils;

import play.db.jpa.JPA;
import play.mvc.Controller;
import service.ServiceInjector;
import dto.DayWeatherDto;

public class WeatherController extends Controller {
	
	public static void weatherforecast(Integer fordays) {
		List<DayWeatherDto> result = new ArrayList<DayWeatherDto>();
		Farmer farmer = AuthController.getFarmer();
		
		if (farmer == null) {
			JsonController.toJson(result);
		}
		
	
		
		Long maxDay = farmer.gameDate.dayOrder + fordays/2;
		Long minDay = farmer.gameDate.dayOrder - fordays/2;
		
		List<Day> days = Day.find("dayOrder>=?1 AND dayOrder<=?2", minDay, maxDay).fetch();
		
		for (Day day:days) {
			DayWeatherDto dayDto = new DayWeatherDto();
			dayDto.id = day.dayOrder.intValue();
			dayDto.date = day.date;
			dayDto.highTemp = day.tempHigh;
			dayDto.lowTemp = day.tempLow;
			dayDto.humidity = day.humidity.intValue();
			dayDto.icon_url = day.weatherType.icon_url;
			dayDto.weatherType = day.weatherType.id;
			dayDto.uvProb = day.uvProb;
			if (dayDto.weatherType == WeatherType.WEATHER_TYPE_RAINY
					|| dayDto.weatherType == WeatherType.WEATHER_TYPE_ICY) {
				dayDto.rainQuantity = ServiceInjector.weatherService
						.getRainingValue(dayDto.date);
			} else {
				dayDto.rainQuantity = 0.0;
			}
			
			if (day.dayOrder < farmer.gameDate.dayOrder) {
				dayDto.type = "past-future";
			} else 
			if (day.dayOrder == farmer.gameDate.dayOrder) {
				dayDto.type = "today";
			} else
			if (day.dayOrder > farmer.gameDate.dayOrder) {
				dayDto.type = "past-future";
			}
			result.add(dayDto);
		}
		JsonController.toJson(result);
	}
	



}
