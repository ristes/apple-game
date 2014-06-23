package controllers;

import java.util.Calendar;
import java.util.List;

import dto.C;
import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Yield;
import play.mvc.Controller;

public class GrowController extends Controller {

	public static String evaluatePlantImage(Farmer farmer) {
		String image_path = "/public/images/game/apple_tree/";
		String additional = "";
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
		additional = checkToPutApplesOnTree(farmer, year, year_level,month,season);
		additional = checkWhiteSprayed(farmer, year_level,season);
		return image_path + String.valueOf(year_level) + String.valueOf(season)
				+ additional + ".png";
	}

	
	public static String checkToPutApplesOnTree(Farmer farmer, int year, int year_level, int month, int season) {
		String result = "";
		if (year_level == 3) {
			if (season == 4) {
				if (month == 8) {
					//has been harvesting this year to show apples on plant
					if (Yield.find("byFarmerAndYear", farmer, year).fetch()
							.size() == 0) {
						result = "a";
					}
				}
			}
		}
		return result;
	}
	
	public static String checkWhiteSprayed(Farmer farmer, int year_level, int season) {
		String result ="";
		if (season == C.SEASON_SPRING || season == C.SEASON_WINTER) {
			Item item = Item.find("byName", "BeloMaslo").first();
//			List<ItemInstance> instances = ItemInstance.find("byTypeAndOwnedBy", item,farmer).fetch();
//			for (ItemInstance instance: instances) {
//				if (instance.operation!=null) {
//					
//				}
//			}
			
			List<ExecutedOperation> operations = ExecutedOperation.find("byItemInstance.typeAndField", item,farmer.field).fetch();
			for (ExecutedOperation operation : operations) {
				if (farmer.isSameYear(operation.startDate)) {
					result = "b";
				}
			}
		}
		return result;
	}
}
