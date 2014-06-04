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
import models.Farmer;
import models.Item;
import models.Store;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;

public class IrrigationController extends Controller {

	
	
	

	

	public static void irrigation(String name, String time,
			Boolean hasTensiometers) {
		String storeName = "IrrigationStore";
		// List<Store> stores = Store.find("byName","IrrigationStore").fetch();
		// if (store.size()==0) {
		// error("Irrigation Store does not exists");
		// }
		// List<Item> irrigations = stores.get(0).items;
		List<Item> irrigationTypes = Item.find("byName", name).fetch();
		if (irrigationTypes.size() == 0) {
			error("No such irrigation type");
		}
		Item irrType = irrigationTypes.get(0);
		if (irrType.store.name.equals(storeName)) {
			error("Not exact store");
		}
		redirect("/IrrigationController/" + irrType.name + "?time=" + time);

	}

	public static void BrazdiNavodnuvanje(String time, Boolean hasTensiometers)
			throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		if (!hasTensiometers) {
			farmer.cumulativeHumidity += Integer.parseInt(time)
					* farmer.coef_soil_type * 100.0;
			HumidityController.humiditySetAppUrls(farmer);

		} else {
			//TODO: optimalna vrednost
		}
		farmer.save();
		JsonController.toJson(farmer, "field", "gameDate");
	}
	
	
}
