package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import service.LogFarmerDataService;
import service.ServiceInjector;
import dto.C;
import models.Farmer;
import models.LogFarmerData;

public class WeatherServiceImpl implements WeatherService{

	@Override
	public Double getSunnyEvaporation(Farmer farmer) {
		return WeatherService.SUNNY_EVAPORATION;
	}

	@Override
	public Double getCloudyEvaporation(Farmer farmer) {
		return WeatherService.CLOUDY_EVAPORATION;
	}

	@Override
	public Double getRainingValue(Farmer farmer) {
		Double rainValue = getRainingValue(farmer.gameDate.date);
		ServiceInjector.logFarmerDataService.logRainValue(farmer, rainValue);
		return rainValue;
	}

	@Override
	public Double getPrevDaysValue(Farmer farmer, Integer days) {
		double result = 0.0;
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH)-days);
		List<LogFarmerData> logs = LogFarmerData.find("typelog=?1 AND logdate>=?2 AND logdate<=?3", LogFarmerDataService.RAIN_VALUE, c.getTime(), farmer.gameDate.date).fetch();
		for (LogFarmerData log:logs) {
			result+=log.information;
		}
		return result;
	}
	
	public Double rainCoefForMonth(Date date) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		double avg_rain = coefs.get(C.KEY_RAIN_COEFS)
				.get(c.get(Calendar.MONTH));
		return avg_rain;
	}
	
	public Double rainCoefForMonth(Integer month) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		double avg_rain = coefs.get(C.KEY_RAIN_COEFS).get(month);
		return avg_rain;
	}

	@Override
	public Double getRainingValue(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Integer randm = ServiceInjector.randomGeneratorService.random(DAY_MIN_HOURS, DAY_MAX_HOURS).intValue();
		Double rainValue = rainCoefForMonth(c.get(Calendar.MONTH))
				* randm;
		return rainValue;
	}

}
