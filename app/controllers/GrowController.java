package controllers;

import java.util.Calendar;

import models.Farmer;
import play.mvc.Controller;

public class GrowController extends Controller {

	public static String evaluatePlantImage(Farmer farmer) {
		String image_path = "/public/images/game/apple_tree/";
		int season = WeatherController.season_level(farmer);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(farmer.gameDate.date);
		int year_level = 1;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
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
		if (year_level == 3) {
			if (season == 4) {
				
			}
		}
		return image_path+String.valueOf(year_level)+String.valueOf(season)+".png";
	}

}
