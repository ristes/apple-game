package controllers;

import java.io.IOException;

import models.Farmer;
import models.Store;
import service.FarmerService;
import service.ServiceInjector;
import service.StoreService;
import service.impl.FarmerServiceImpl;
import service.impl.StoreServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;

public class StoreController extends GameController {

	public static void all() throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findAll());
	}

	public static void allNg() {
		StoreService storeSer = new StoreServiceImpl();
		renderJSON(storeSer.findAllStores());
	}

	public static void storeitems() throws JsonGenerationException, JsonMappingException, IOException {
		StoreService service = new StoreServiceImpl();
		renderJSON(service.storeItems());
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



	public static void myitems() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		FarmerService farmerService = new FarmerServiceImpl();
		renderJSON(farmerService.farmersItems(farmer).values());
	}

}
