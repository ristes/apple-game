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

import dto.FarmerTransactionDao;
import dto.ItemBoughtDto;
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

	public static void buyItem(String itemName, Double quantity,
			String currentState) throws IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer != null) {
			Item item = Item.find("byName", itemName).first();
			Boolean successTransaction = triggerBuyingItem(farmer, item,
					quantity);

			if (successTransaction) {
				farmer.currentState = currentState;
				farmer.save();
				JsonController.farmerJson(farmer);
			}
		}

		JsonController.toJson("");
	}

	public static Boolean triggerBuyingItem(Farmer farmer, Item item,
			double quantity) {
		double value = item.price * quantity;

		if (farmer.balans < value) {
			return false;
		}
		farmer.balans -= value;
		ItemInstance instance = new ItemInstance();
		instance.ownedBy = farmer;
		instance.type = item;
		instance.quantity = quantity;
		instance.save();

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

	public static void myitems() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		List<ItemBoughtDto> result = new ArrayList<ItemBoughtDto>();
		String sql = "SELECT ItemInstance.id, ItemInstance.type_id, Item.name, Item.imageurl, count(ItemInstance.type_id) as count, "
				+ "ItemInstance.quantity, Store.name as store  FROM ItemInstance, Item, Store where ItemInstance.type_id=Item.id "
				+ "and ItemInstance.ownedBy_id=:farmer_id and Store.id=Item.store_id and ItemInstance.id NOT IN "
				+ "(select DISTINCT(itemInstance_id) FROM ExecutedOperation where field_id=:field_id and "
				+ "not(isnull(ItemInstance_id))) GROUP BY type_id order by ItemInstance.id";
		// String sqlSelect =
		// "select * from ItemInstance  where ownedBy_id=:farmer_id and id NOT IN (select DISTINCT(itemInstance_id) FROM ExecutedOperation where field_id=:field_id and  not(isnull(ItemInstance_id)))";
		List<Object[]> resultSql = JPA.em().createNativeQuery(sql)
				.setParameter("farmer_id", farmer.id)
				.setParameter("field_id", farmer.field.id).getResultList();
		for (Object[] obj : resultSql) {
			ItemBoughtDto item = new ItemBoughtDto();
			item.id = ((BigInteger) obj[0]).longValue();
			item.type_id = ((BigInteger) obj[1]).longValue();
			item.name = (String) obj[2];
			item.url = (String) obj[3];
			item.count = ((BigInteger) obj[4]).intValue();
			if (obj[5] != null) {
				item.quantity = ((Double) obj[5]).doubleValue();
			} else {
				item.quantity = null;
			}
			item.store = (String) obj[6];
			result.add(item);

		}
		renderJSON(result);
	}

}
