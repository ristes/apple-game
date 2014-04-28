package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import models.Day;

import play.Play;
import play.mvc.Controller;

public class Weather extends Controller {
	
	public final static int TEMPERATURE_LOW_MIN = 1;
	public final static int TEMPERATURE_HIGH_MAX = 6; 

	/**
	 * if bias > 0.5, it's more likely to have bad year otherwise, probably it
	 * will be good year
	 */
	public static HashMap<Integer, Double> biasForBadYear;
	
	/**
	 * represents the bias of weather type for every month
	 */
	public static HashMap<Integer, Double> biasForWeatherType;

	private static Double normalize(Double result,Double minValue, Double maxValue) {
		return (result - minValue)/(maxValue-minValue);
	}
	
	/**
	 * mathod that generates random number with biasing
	 * if low = 0.0, high = 1.0 and bias = 1.0 the average is 0.5
	 * if low = 0.0, high = 1.0 and bias = 2.0 the average is 0.25
	 * if low = 0.0, high = 1.0 and bias = 0.5 the average is 0.75
	 * @param low
	 * @param high
	 * @param bias
	 * @return
	 */
	private static Double random(Double low, Double high, Double bias) {
		Double r= Math.random();
		r = Math.pow(r, bias);
		return low + (high-low)*r;
	}
	
	public static void generateValuesForHashSetBadYear(Integer endYear) {
		biasForBadYear = new HashMap<Integer, Double>();
		Calendar c = Calendar.getInstance();
		Integer year = c.get(Calendar.YEAR);
		for (int i = year; i <= endYear; i++) {
//			Double r = Math.random();
//			Double r1 = null;
//			if (r > 0.7) {
//				r1 = 0.8 + (Math.random() * 0.5);
//			} else {
//				r1 = (Math.random() * 0.5);
//			}
//			biasForBadYear.put(year, r1);
			Double r = random(0.0, 1.0, 1.75);
			biasForBadYear.put(i, r*2);
		}
		renderJSON(biasForBadYear);
	}
	
	public static void representBias() {
		generateValuesForHashSetBadYear(2040);
		renderJSON(biasForBadYear);
	}

	public static Double generateHeavyRain(Day day) {
		Date startFrom = null;
		Date endTo = null;
		Calendar c = Calendar.getInstance();
		c.setTime(day.date);
		Integer year = c.get(Calendar.YEAR);
		c.set(Calendar.MONTH, 6);
		c.set(Calendar.DAY_OF_MONTH, 1);
		startFrom = c.getTime();

		c.set(Calendar.MONTH, 9);
		c.set(Calendar.DAY_OF_MONTH, 1);
		endTo = c.getTime();

		Double result = 0.0;

		if ((day.date.compareTo(startFrom) > 0)
				&& (day.date.compareTo(endTo) < 0)) {
			Double bias = biasForBadYear.get(year);
			if (day.weatherType.name.equals("rainy")) {
				if (bias > 0.5) {
					result = bias + (Math.random() * 0.2);
					result = normalize(result,0.5,1.2);
				} else {
					result = Math.random()*0.4;
				}
				
				result = 1-random(0.0, 1.0, biasForBadYear.get(year));
			}
		}
		return result;
	}

	

	public static void generateWeather(Integer endYear) {

		generateValuesForHashSetBadYear(endYear);
		for (int i=Calendar.getInstance().get(Calendar.YEAR);i<=endYear;i++) {
			File file = Play.getFile("/data/weathermodel.csv");
			BufferedReader br = null;
		}
	}
	
	

}
