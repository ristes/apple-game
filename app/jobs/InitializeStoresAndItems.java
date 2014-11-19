package jobs;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Item;
import models.ItemType;
import models.Store;
import play.Play;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

@OnApplicationStart
public class InitializeStoresAndItems extends Job {

	Map<String, Store> storeMap = new HashMap<String, Store>();
	Map<String, Item> itemsMap = new HashMap<String, Item>();
	Map<String, ItemType> itemsTypes = new HashMap<String, ItemType>();

	@Override
	public void doJob() throws Exception {

		List<Store> stores = readStores("/data/stores.json");
		List<Item> items = readItems("data/storeItems.json");
		System.out.println(stores);

		List<Store> dbStores = Store.all().fetch();
		for (Store s : dbStores) {
			storeMap.put(s.name, s);
		}

		List<Item> dbItems = Item.all().fetch();
		for (Item vs : dbItems) {
			itemsMap.put(vs.name, vs);
		}

		for (Store s : stores) {
			if (!storeMap.containsKey(s.name)) {
				s.save();
				storeMap.put(s.name, s);
				System.out.println("Saving store: " + s.name);
			}
		}
		for (Item item : items) {
			if (!itemsMap.containsKey(item.name)) {
				item.store = storeMap.get(item.storeName);
				item.save();
				itemsMap.put(item.name, item);
				System.out.println("Saving item: " + item.name);
			}
		}
		super.doJob();
	}

	private List<Store> readStores(String jsonFileName) throws Exception {
		File storesJson = Play.getFile(jsonFileName);
		Type listType = new TypeToken<ArrayList<Store>>() {
		}.getType();
		System.out.println(listType);
		ArrayList<Store> res = new GsonBuilder().create().fromJson(
				new FileReader(storesJson), listType);
		return res;
	}

	private List<Item> readItems(String jsonFileName) throws Exception {
		File storesJson = Play.getFile(jsonFileName);
		Type listType = new TypeToken<ArrayList<Item>>() {
		}.getType();
		System.out.println(listType);
		ArrayList<Item> res = new GsonBuilder().create().fromJson(
				new FileReader(storesJson), listType);
		return res;
	}
}
