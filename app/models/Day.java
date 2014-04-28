package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;


import play.db.jpa.Model;


@Entity
public class Day extends Model{
	
	public Date date;
	
	@ManyToOne
	public WeatherType weatherType;
	
	public Double tempLow;
	
	public Double tempHigh;
	
	public Long humidityOfLeaf;
	
	public Double iceProb;
	
	public Long humidity;
	
	/**
	 * if above 0.5, it's high UV radiation
	 * otherwise, low UV radiation
	 */
	
	public Double uvProb;
	
	/**
	 * if above 0.0 and below 0.2 there is 0% loss
	 * if above 0.2 and below 0.5 there is 25% loss
	 * if above 0.5 and below 0.8 there is 50% loss
	 * if above 0.8 and below 1 there is 80% loss 
	 */
	public Double heavyRain;

}
