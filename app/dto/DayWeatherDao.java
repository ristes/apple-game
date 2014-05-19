package dto;

import java.util.Date;

import models.WeatherType;

public class DayWeatherDao {
	
	public int id;
	
	public Date date;

	public Long weatherType;
	
	public String icon_url;
	
	
	public Double lowTemp;
	
	public Double highTemp;
	
	public Double uvProb;
	
	public Integer humidity;
	
	//past,today,future
	public String type;
	
}
