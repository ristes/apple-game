package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import exceptions.NotEnoughMoneyException;
import models.Farmer;
import models.Item;
import models.Store;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;

public class IrrigationController extends Controller {

	public static void irrigation(String name, String time,
			Boolean hasTensiometers) throws JsonGenerationException,
			JsonMappingException, IOException {
		String storeName = "IrrigationStore";
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		List<Item> irrigationTypes = Item.find("byName", name).fetch();
		if (irrigationTypes.size() == 0) {
			error("No such irrigation type");
		}
		Item irrType = irrigationTypes.get(0);
		if (irrType.store.name.equals(storeName)) {
			error("Not exact store");
		}
		if (name.equals("BrazdiNavodnuvanje")) {
			try {
				if (!hasTensiometers) {
					try {
						Double deltaCul = 0.0;
						deltaCul = brazdiNavodnuvanje(farmer, time);
						farmer.deltaCumulative += deltaCul;
					} catch (NotEnoughMoneyException ex) {
						JsonController.toJson(farmer, "field", "gameDate");
						ex.printStackTrace();
					} finally {
						farmer.save();
					}
				} else {
					double quantity = irrigateTensiometers(farmer);
					farmer.deltaCumulative = quantity;
					farmer.cumulativeHumidity = 0.0;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				if (!hasTensiometers) {
					Double deltaCul = dropsNavodnuvanje(farmer, time);
					farmer.deltaCumulative += deltaCul;
				} else {
					farmer.deltaCumulative = 0.0;
					farmer.cumulativeHumidity = 0.0;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		HumidityController.humiditySetAppUrls(farmer);
		farmer.save();
		JsonController.toJson(farmer, "field", "gameDate","weatherType");

	}

	public static double dropsNavodnuvanje(Farmer farmer, String time) throws NotEnoughMoneyException {
		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();
		double result = 0.0;
		if (farmer.field.hasDropSystem(farmer)) {
			int timeInt = Integer.parseInt(time);
			int coefSoil = farmer.coef_soil_type;
			double parH = coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_DROPS);
			result = timeInt * coefSoil * parH;
			Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES).get(C.ENUM_DROPS)*farmer.field.area*timeInt;
			if (price<=farmer.balans) {
				farmer.balans -= price;
			} else {
				throw new NotEnoughMoneyException("Not enough money to irrigate.");
			}
		} 
		/*
		else {
			result = Integer.parseInt(time)
					* farmer.coef_soil_type
					* coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
							C.ENUM_GROOVES);
		}
		*/
		return result;
	}
	
	public static double brazdiNavodnuvanje(Farmer farmer, String time) throws NotEnoughMoneyException{
		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();
		double result = 0.0;
		result = Integer.parseInt(time)
					* farmer.coef_soil_type
					* coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
							C.ENUM_GROOVES);
		Double area_size = farmer.field.area;
		Double irr_value = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES).get(C.ENUM_DROPS).doubleValue();
		Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES).get(C.ENUM_DROPS).doubleValue()*area_size*Integer.parseInt(time);
		if (price<=farmer.balans) {
			farmer.balans -= price;
		} else {
			throw new NotEnoughMoneyException("Not enough money to irrigate.");
		}
		
		
		return result;
	}
	
	public static double irrigateTensiometers(Farmer farmer) throws NotEnoughMoneyException {
		HashMap<String,ArrayList<Double>> coefs = HumidityController.load_hash();
		double delta = farmer.deltaCumulative;
		double quantity = 0.0;
		if (farmer.field.hasDropSystem(farmer)) {
			int time = (int) (delta/coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_DROPS));
			quantity = dropsNavodnuvanje(farmer,String.valueOf(time));
		} else {
			int time = (int) (delta/coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_GROOVES));
			quantity = dropsNavodnuvanje(farmer,String.valueOf(time));
		}
		return quantity;
	}

}
