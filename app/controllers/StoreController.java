package controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Item;
import models.Operation;
import models.OperationDuration;
import models.Store;
import play.mvc.Controller;

public class StoreController extends Controller {

	public static void all() throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findAll());
	}

	public static void show(Long storeId) throws JsonGenerationException,
			JsonMappingException, IOException {
		JsonController.toJson(Store.findById(storeId));
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
