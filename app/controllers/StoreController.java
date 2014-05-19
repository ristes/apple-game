package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.FarmerTransactionDao;
import dto.StoreDto;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Operation;
import models.OperationDuration;
import models.Store;
import play.cache.Cache;
import play.mvc.Controller;

public class StoreController extends Controller {

	public static void all() throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findAll());
	}

	public static void allNg() {
		List<StoreDto> result = new ArrayList<StoreDto>();
		List<Store> stores = Store.findAll();
		for (Store store : stores) {
			StoreDto dto = new StoreDto();
			dto.id = store.id;
			dto.name = store.name;
			dto.url = store.image.url;
			result.add(dto);
		}
		renderJSON(result);
	}

	public static void show(Long storeId) throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findById(storeId));
	}

	public static void showitems(Long storeId) throws IOException {
		Store store = Store.findById(storeId);
		JsonController.toJson(store.items);
		
	}

	public static void buyItem(String itemid, Integer quantity,
			String currentState) throws IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer != null) {
			Item item = Item.findById(Long.parseLong(itemid));
			Long cost = (long) item.price * quantity;
			Boolean successTransaction = triggerBuyingItem(farmer, item,
					quantity);

			if (successTransaction) {
				farmer.currentState = currentState;
				farmer.save();
				JsonController.toJson(farmer);
			}
		}

		JsonController.toJson("");
	}

	public static Boolean triggerBuyingItem(Farmer farmer, Item item,
			int quantity) {
		long value = item.price * quantity;

		if (farmer.balans < value) {
			return false;
		}
		farmer.balans -= value;
		for (int i = 0; i < quantity; i++) {
			ItemInstance instance = new ItemInstance();
			instance.ownedBy = farmer;
			instance.type = item;
			instance.save();

		}
		farmer.save();
		return true;
	}

	/**
	 * Return all the items from the store
	 * 
	 * @param store
	 */
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

}
