package service.impl;

import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import service.PlantingService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class PlantingServiceImpl implements PlantingService{

	@Override
	public Farmer savePlantingParams(Farmer farmer, String array, Integer seedlings) {
		int optimum = 2380;
		int curQuantity = farmer.field.plantation.currentQuantity;
		optimum = (farmer.field.plantation.base.maxTreePerHa + farmer.field.plantation.base.minTreePerHa)/2;
		farmer.field.plantation.fieldPercentage = (int) (curQuantity * 100.0 / optimum);
		farmer.field.plantation.treePositions = addTypesOfSeedlingsToJSONArray(farmer, array);
		farmer.field.plantation.save();
		return farmer;
	}
	
	public String addTypesOfSeedlingsToJSONArray(Farmer farmer, String array) {
		JsonParser parser = new JsonParser();
		JsonElement jsonEl = parser.parse(array);
		JsonArray jsonAr = jsonEl.getAsJsonArray();
		int total = jsonAr.size();
		List<PlantationSeedling> mySeedls = PlantationSeedling.find("byPlantation", farmer.field.plantation).fetch();
		Integer[] mySeedlPercs = new Integer[mySeedls.size()];
		JsonArray resultArr = new JsonArray();
		for (int i=0;i<mySeedlPercs.length;i++){
			mySeedlPercs[i]=mySeedls.get(i).percentOfPlantedArea*jsonAr.size()/100;
		}
		int globalCounter = 0;
		for (int i=0;i<mySeedlPercs.length;i++) {
			for (int j=globalCounter;j<globalCounter + mySeedlPercs[i];j++) {
				JsonObject jsonObject = jsonAr.get(j).getAsJsonObject();
				jsonObject.addProperty("color", mySeedls.get(i).seedling.type.apple_color);
				resultArr.add(jsonObject);
			}
			globalCounter+=mySeedlPercs[i];
		}
		if (globalCounter<total) {
			for (int i=globalCounter;i<total;i++) {
				JsonObject jsonObject = jsonAr.get(i).getAsJsonObject();
				jsonObject.addProperty("color", mySeedls.get(mySeedlPercs.length-1).seedling.type.apple_color);
				resultArr.add(jsonObject);
			}
		}
		
		return resultArr.toString();
		
	}
	

}
