package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import models.Day;
import models.WeatherType;

import play.Play;
import play.mvc.Controller;

public class Weather extends Controller {
	
	public final static int DATE_MODEL  = 0;
	public final static int TEMPERATURE_LOW_MIN = 1;
	public final static int TEMPERATURE_HIGH_MAX = 6; 
	public final static int UV_PROBABILITY = 7;
	public final static int ICE_PROBABILITY = 8;
	public final static int RAIN_PROBABILITY = 9;
	public final static int HUMIDITY_LEAF_MIN = 10;
	public final static int HUMIDITY_AVG = 11;
	
	public final static long WEATHER_TYPE_SUNNY = 1;
	public final static long WEATHER_TYPE_CLOUDY = 2;
	public final static long WEATHER_TYPE_RAINY = 3;
	public final static long WEATHER_TYPE_ICY = 4;

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
	
	private static Double noise(Double value, Double range) {
		Double random = Math.random();
		Double plusOrMinus = Math.random();
		if (plusOrMinus>0.5) {
			value = value + (range * random);
		}
		else {
			value = value - (range * random);
		}
		return value;
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
	
	public static HashMap<Integer, Double> generateValuesForHashSetBadYear(Integer endYear) {
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
		return biasForBadYear;
	}
	
	public static void representBias() {
		generateValuesForHashSetBadYear(2040);
		renderJSON(biasForBadYear);
	}

	public static Day generateHumidity(Double humAvg, Day day) {
		if (day.weatherType.id == WEATHER_TYPE_RAINY || day.weatherType.id==WEATHER_TYPE_ICY) {
			humAvg = normalize((humAvg + random(-5.0,5.0,0.75)),-5.0,105.0)*100;
		} else {
			humAvg = normalize((humAvg + random(-5.0,5.0,1.25)),-5.0,105.0)*100;
		}
		day.humidity = Math.round(humAvg);
		return day;
	}
	
	public static Day generateHumidityOfLeaf(Double humLeaf, Day day) {
		if (day.weatherType.id == WEATHER_TYPE_RAINY) {
			humLeaf = normalize((humLeaf + random(-200.0,200.0,0.75)),-200.0,1640.0)*1400;
		} else {
			humLeaf = normalize((humLeaf + random(-200.0,200.0,1.25)),-200.0,1640.0)*1400;
		}
		day.humidityOfLeaf = Math.round(humLeaf);
		return day;
	}
	
	public static Day generateUV(Double uvProb, Day day) {
		if (day.weatherType.id == WEATHER_TYPE_SUNNY) {
			uvProb = normalize((uvProb + random(-0.1,0.1,0.75)),-0.1,1.1);
		} else {
			uvProb = normalize((uvProb + random(-0.1,0.1,1.75)),-0.1,1.1);
		}
		day.uvProb = uvProb;
		return day;
	}
	
	public static Day generateHeavyRain(Day day) {
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
//				if (bias > 0.5) {
//					result = bias + (Math.random() * 0.2);
//					result = normalize(result,0.5,1.2);
//				} else {
//					result = Math.random()*0.4;
//				}
				
				result = 1-random(0.0, 1.0, biasForBadYear.get(year));
			}
		}
		day.heavyRain = result;
		return day;
	}

	public static Day createWeatherForDay(Integer year, String line, Long dayOrder) {
		String[] fields = line.split(",");
		
		String date = fields[DATE_MODEL];
		
		Double iceProb = Double.valueOf(fields[ICE_PROBABILITY]);
		Double rainProb = Double.valueOf(fields[RAIN_PROBABILITY]);
		
		Double minTemp = Double.valueOf(fields[TEMPERATURE_LOW_MIN]);
		Double maxTemp = Double.valueOf(fields[TEMPERATURE_HIGH_MAX]);
		
		Double uvProb = Double.valueOf(fields[UV_PROBABILITY]);
		
		Double humAvg = Double.valueOf(fields[HUMIDITY_AVG]);
		Double humLeaf = Double.valueOf(fields[HUMIDITY_LEAF_MIN]);
		
		String[] dateFields = date.split("-");
		if (dateFields.length!=2) {
			System.out.println("Problems in date format in csv file");
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.DAY_OF_MONTH,Integer.parseInt(dateFields[0]));
		c.set(Calendar.MONTH, Integer.parseInt(dateFields[1])-1);
		
		Day day = new Day();
		day.date = c.getTime();

		minTemp = noise(minTemp,2.0);
		maxTemp = noise(maxTemp,5.0);
		
		if (iceProb==0.0) { //it is not winter or spring
		
			rainProb = normalize(noise(rainProb,0.1),-0.1,0.6);
			if (rainProb<0.25) {
				if (rainProb>0.15) {
					//it's cloudy
					WeatherType wType = WeatherType.findById(WEATHER_TYPE_CLOUDY);
					day.weatherType = wType;
				}
				else {
					WeatherType wType = WeatherType.findById(WEATHER_TYPE_SUNNY);
					day.weatherType = wType;
				}
			} else {
				WeatherType wtype = WeatherType.findById(WEATHER_TYPE_RAINY);
				day.weatherType = wtype;
			}
		} else {
			iceProb = noise(iceProb,0.1);
			WeatherType wType = null;
			if (iceProb>0.45) {
				if (maxTemp>0.0) {
					maxTemp = noise(-5.0,10.0);
				}
				wType = WeatherType.findById(WEATHER_TYPE_ICY);
				day.weatherType = wType;
			} else {
				if (rainProb>0.25) {
					wType  = WeatherType.findById(WEATHER_TYPE_RAINY);
				} else {
					wType = WeatherType.findById(WEATHER_TYPE_CLOUDY); 
				}
			}
			day.weatherType = wType;
		}
		day.iceProb = iceProb;
		day.tempLow = minTemp;
		day.tempHigh = maxTemp;
		day = generateHumidity(humAvg,day);
		day = generateHumidityOfLeaf(humLeaf,day);
		day = generateHeavyRain(day);
		day = generateUV(uvProb,day);
		day.dayOrder =dayOrder;
		return day;
		
		
	}

	public static void generateWeather(String startYear, String endYear) {
		
		Long dayOrder = (long)0;
		Integer startYearInt = Integer.parseInt(startYear);
		Integer endYearInt = Integer.parseInt(endYear);
		HashMap<Integer, Double> biasForYear = generateValuesForHashSetBadYear(endYearInt);
		for (int i=startYearInt;i<=endYearInt;i++) {
			File file = Play.getFile("/data/weathermodel.csv");
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line=br.readLine())!=null) {
					Day day = createWeatherForDay(i,line,dayOrder);
					day.save();
					dayOrder++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	

}
