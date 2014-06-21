package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.StatusDto;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import models.Farmer;
import models.Item;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;

public class LandTreatmanController extends Controller {
	
	public static void digging() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login?locale=mk");
		}
		try {
			farmer = executeDigging(farmer);
		} catch (NotEnoughMoneyException ex) {
			StatusDto status = new StatusDto(false, ex.getMessage(), String.valueOf(ex.getPrice()), farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		}
		farmer.save();
		StatusDto status = new StatusDto(true,null,null,farmer);
		JsonController.toJson(status, "field", "gameDate","weatherType");
	}

	private static Farmer executeDigging(Farmer farmer) throws NotEnoughMoneyException{
		Item digItem = Item.find("byName", "DiggingItem").first();
		Integer price = (int)(farmer.field.area * digItem.price);
		if (price>farmer.balans) {
			throw new NotEnoughMoneyException(Messages.get("controller.digging.fail.notenoughmoney"),price);
		}
		farmer.balans -= price;
		farmer.digging_coef = 0.0;
		farmer.productQuantity += farmer.productQuantity*0.05;
		return farmer;
	}
	
	public static int diggingLevel(Farmer farmer) {
		Double coef = farmer.digging_coef;
		if (coef>2.0) {
			return 3;
		}
		return coef.intValue()+1;
	}

	/**
	 * return 3 - high need of plowing return 2 - medium return 1 - not need
	 * 
	 * @param farmer
	 * @return
	 */
	public static int plowingLevel(Farmer farmer) {
		if (farmer.grass_growth > 8) {
			return 4;
		} else if (farmer.grass_growth > 5) {
			return 3;
		} else if (farmer.grass_growth > 2) {
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
		try {
			farmer = determineThePlowingPrice(farmer);
		} catch (TooWaterOnFieldException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.deltaCumulative),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		} catch (SoilTooDryException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.deltaCumulative),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		} catch (NotEnoughMoneyException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.balans),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		}
		farmer.grass_growth = 0.0;
		farmer.evaluateState();
		farmer.save();
		StatusDto status = new StatusDto(true,null,null,farmer);
		JsonController.toJson(status, "field", "gameDate","weatherType");
		
	}

	private static Farmer determineThePlowingPrice(Farmer farmer)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException {
		int level = HumidityController.humidityLevel(farmer);
		Item plowing = (Item) Item.find("byName", "PlowingItem").fetch().get(0);
		Integer price = 0;
		switch (level) {
		case 0:
			throw new SoilTooDryException(Messages.get("controller.plowing.fail.toodry"));
		case 1:
			price =  1 * plowing.price * (int) farmer.field.area;
			break;
		case 2:
			price =  (int) (1.3 * plowing.price * (int) farmer.field.area);
			break;
		case 3:
			price =  (int) (1.5 * plowing.price * (int) farmer.field.area);
			break;
		case 4:
			throw new TooWaterOnFieldException(Messages.get("controller.plowing.fail.toowater"));
		}
		if (price > farmer.balans) {
			throw new NotEnoughMoneyException(Messages.get("controller.plowing.fail.notenoughmoney"));
		}
		farmer.balans -= price;
		return farmer;
	}

}
