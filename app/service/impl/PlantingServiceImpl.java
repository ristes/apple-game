package service.impl;

import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import service.PlantingService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;


public class PlantingServiceImpl implements PlantingService{

	@Override
	public Farmer savePlantingParams(Farmer farmer, String array, Integer seedlings) {
		int optimum = 2380;
		int curQuantity = farmer.field.plantation.currentQuantity;
		optimum = (farmer.field.plantation.base.maxTreePerHa + farmer.field.plantation.base.minTreePerHa)/2;
		farmer.field.plantation.fieldPercentage = (int) (curQuantity * 100.0 / optimum);
		farmer.field.plantation.treePositions = array;
		
		farmer.field.plantation.save();
		return farmer;
	}
	/*
	public String addTypesOfSeedlingsToJSONArray(Farmer farmer, String array) {
		JsonParser parser = new JsonParser();
		JsonElement jsonEl = parser.parse(array);
		JsonArray jsonAr = jsonEl.getAsJsonArray();
		int total = jsonAr.size();
		List<PlantationSeedling> mySeedls = PlantationSeedling.find("byPlantation", farmer.field.plantation).fetch();
		Integer[] mySeedlPercs = new Integer[mySeedls.size()];
		for (int i=0;i<mySeedlPercs.length;i++) {
			
			for (int j =0;j < jsonAr.size();j++) {
				
			}
		}
		*/
	

}
