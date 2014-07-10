package controllers;

import java.util.Calendar;

import play.mvc.Controller;
import models.Farmer;

public class YieldController extends Controller{
	
	public final static int START_YEAR = 2022; 

	public static Double calculateYield() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int year = c.get(Calendar.YEAR);
		if (WeatherController.evaluateYearLevel(year)>1){
			double applesPerA = farmer.field.plantation.base.maxApplesPerHa;
			return applesPerA * farmer.field.area * 1000*(farmer.field.plantation.fieldPercentage/100.0);
		}
		return 0.0;

	}
	
}
