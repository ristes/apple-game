package controllers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import models.Farmer;
import models.Item;
import models.Store;
import play.mvc.Controller;

public class IrrigationController extends Controller{
	
	/**
	 * determinates humidity level of soil
	 * 1 - low
	 * 2 - medium
	 * 3 - high
	 * @param farmer
	 * @return
	 */
	public static int humidityLevel(Farmer farmer) {
		Double cumVal = farmer.cumulativeHumidity;
		if (cumVal<1000) {
			return 1;
		}
		if (cumVal>=1000 &&cumVal<2000) {
			return 2;
		}
		return 3;
	}
	
	public static Farmer humiditySetAppUrls(Farmer farmer) {
		int level = humidityLevel(farmer);
		if (level==1) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_low];
		}
		if (level==2) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_normal];
		}
		if (level==3) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_high];
		}
		return farmer;
	}
	
	public static void irrigation(String name, String time) {
		String storeName = "IrrigationStore";
//		List<Store> stores = Store.find("byName","IrrigationStore").fetch();
//		if (store.size()==0) {
//			error("Irrigation Store does not exists");
//		}
//		List<Item> irrigations = stores.get(0).items;
		List<Item> irrigationTypes = Item.find("byName", name).fetch();
		if (irrigationTypes.size()==0) {
			error("No such irrigation type");
		}
		Item irrType = irrigationTypes.get(0);
		if (irrType.store.name.equals(storeName)) {
			error("Not exact store");
		}
		redirect("/IrrigationController/"+irrType.name+"?time="+time);
		
	}
	
	public static void BrazdiNavodnuvanje(String time) throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		farmer.cumulativeHumidity+=Integer.parseInt(time)*farmer.coef_soil_type*100.0;
		humiditySetAppUrls(farmer);
		farmer.save();
		JsonController.toJson(farmer, "field", "gameDate");
	}

}
