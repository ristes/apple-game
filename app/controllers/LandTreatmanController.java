package controllers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Farmer;
import models.Item;
import play.mvc.Controller;

public class LandTreatmanController extends Controller{
	
	public static int diggingLevel(Farmer farmer) {
		Double coef = farmer.digging_coef;
		return coef.intValue();
	}
	
	/**
	 * return 3 - high need of plowing
	 * return 2 - medium
	 * return 1 - not need
	 * @param farmer
	 * @return
	 */
	public static int plowingLevel(Farmer farmer) {
		if (farmer.grass_growth>8) {
			return 4;
		} else if (farmer.grass_growth>5) {
			return 3;
		} else if (farmer.grass_growth>2) {
			return 2;
		}
		return 1;
	}
	
	public static void plowing() throws JsonGenerationException, JsonMappingException, IOException {
	
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		Integer balance = farmer.balans;
		Integer expense;
		Item plowing = (Item)Item.find("byName", "PlowingItem").fetch().get(0);
		String image = "";
		//the price depends on humidity of soil
		if (farmer.cumulativeHumidity>=1500) {
			expense = 3 * plowing.price * (int)farmer.field.area;
			
		} else if (farmer.cumulativeHumidity <1500 && farmer.cumulativeHumidity >=1000 ) {
			expense = 2 * plowing.price * (int)farmer.field.area;
		} else if (farmer.cumulativeHumidity < 1000 && farmer.cumulativeHumidity >=500) {
			expense = 1 * plowing.price * (int)farmer.field.area;
		} else {
			expense = 2 * plowing.price * (int)farmer.field.area;
		}
		farmer.balans -= expense;
		farmer.grass_growth = 0.0;
		farmer.save();
		
		JsonController.toJson(farmer, "field", "gameDate");
		
	}

}
