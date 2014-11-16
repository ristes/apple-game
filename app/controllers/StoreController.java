package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.FarmerTransactionDto;
import dto.ItemBoughtDto;
import dto.StatusDto;
import dto.StoreDto;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Operation;
import models.OperationDuration;
import models.Store;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.mvc.Controller;
import service.FarmerService;
import service.StoreService;
import service.impl.FarmerServiceImpl;
import service.impl.StoreServiceImpl;

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
		StoreService storeService = new StoreServiceImpl();
		StatusDto status = storeService.buyItem(farmer, itemName, quantity, currentState);
		status.message = "Успешно купена ставка.";
		status.additionalInfo=String.valueOf(farmer.sumOfApples());
		
		JsonController.statusJson(status);
	}

	
/*
	/**
	 * Return all the items from the store
	 * 
	 * @param store
	 */
	/*
	public static void storeData(Long storeId) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Store store = Store.findById(storeId);
		Set<Item> items = new HashSet<Item>(store.items);
		Set<Operation> operations = new HashSet<Operation>();
		Set<OperationDuration> durations = new HashSet<OperationDuration>();

		result.put("Store", store);
		result.put("Opeartion", operations);
		result.put("Item", items);
		result.put("OperationDuration", durations);

		if (store.items != null) {
			for (Item item : store.items) {

				if (item.attachedTo != null) {
					items.add(item.attachedTo);
				}
				if (item.attachments != null) {
					items.addAll(item.attachments);
				}

				if (item.operation != null) {
					operations.add(item.operation);
					if (item.operation.durations != null) {
						durations.addAll(item.operation.durations);
					}
				}
			}
		}
		JsonController.toJson(result);

	}
	*/

	public static void myitems() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		FarmerService farmerService = new FarmerServiceImpl();
		renderJSON(farmerService.farmersItems(farmer));
	}

}
