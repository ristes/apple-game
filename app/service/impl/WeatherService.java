package service.impl;

import java.util.Date;

import models.Farmer;

public interface WeatherService {
	
	
	public static final int WEATHER_TYPE_SUNNY = 1;
	public static final int WEATHER_TYPE_CLOUDY = 2;
	public static final int WEATHER_TYPE_RAINY = 3;
	public static final int WEATHER_TYPE_ICY = 4;
	
	public static final Double DAY_MIN_HOURS = 0.0;
	public static final Double DAY_MAX_HOURS = 4.0;
	
	public static final Double SUNNY_EVAPORATION = 5.0;
	public static final Double CLOUDY_EVAPORATION = 2.0;
	
	public Double getSunnyEvaporation(Farmer farmer);
	
	public Double getCloudyEvaporation(Farmer farmer);
	
	public Double getRainingValue(Farmer farmer);
	public Double getPrevDaysValue(Farmer farmer, Integer days);

	public Double getRainingValue(Date date);

}
