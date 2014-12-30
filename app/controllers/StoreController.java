package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Farmer;
import models.Item;
import models.Store;
import service.FarmerService;
import service.ServiceInjector;
import service.impl.FarmerServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dao.DaoInjector;
import dto.StatusDto;
import dto.StoreItemDto;

public class StoreController extends GameController {

	public static void all() throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findAll());
	}

	public static void allNg() {
		renderJSON(ServiceInjector.storeService.findAllStores());
	}

	public static void storeitems() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		renderJSON(ServiceInjector.storeService.storeItems(farmer));
	}
	
	public static void show(Long storeId) throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findById(storeId));
	}

	public static void showitems(Long storeId) throws IOException {
		Store store = Store.findById(storeId);
		JsonController.toJson(store.items);

	}

	public static void buyItem(String itemName, Double quantity,
			String currentState) throws IOException {
		Farmer farmer = AuthController.getFarmer();
		StatusDto status = ServiceInjector.storeService.buyItem(farmer, itemName, quantity, currentState);
		status.message = "Успешно купена ставка.";
//		status.additionalInfo=String.valueOf(farmer.());
		
		JsonController.statusJson(status);
	}

	
	public static void unboughtitems() throws JsonGenerationException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}

		renderJSON(ServiceInjector.storeService.storeItems(farmer));
	}
	
	

	public static void myitems() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		renderJSON(ServiceInjector.farmerService.farmersItems(farmer).values());
	}

}
