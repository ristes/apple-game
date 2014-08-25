package service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controllers.JsonController;
import models.Base;
import models.Farmer;
import models.Field;
import models.Item;
import models.PlantType;
import models.Plantation;
import models.Seedling;
import models.SeedlingType;
import models.Store;
import models.Terrain;
import models.TerrainSize;
import dto.StoreDto;
import dto.StoreItemDto;
import exceptions.NotEnoughMoneyException;
import service.DispensibleItemTransaction;
import service.ItemTransactionService;
import service.MoneyTransactionService;
import service.StoreService;

public class StoreServiceImpl implements StoreService {

	@Override
	public List<StoreDto> findAllStores() {
		List<StoreDto> result = new ArrayList<StoreDto>();
		List<Store> stores = Store.findAll();
		for (Store store : stores) {
			StoreDto dto = new StoreDto();
			dto.id = store.id;
			dto.name = store.name;
			dto.url = store.image.url;
			result.add(dto);
		}
		return result;
	}

	public Farmer buyItem(Farmer farmer, String itemName, Double quantity,
			String currentState){
		if (farmer != null) {
			Item item = Item.find("byName", itemName).first();
			ItemTransactionService itemTrans = new TransactionServiceImpl();
			try {
				farmer = itemTrans.commitBuyingItem(farmer, item, quantity);
			} catch (NotEnoughMoneyException ex) {
				ex.printStackTrace();
			}
			farmer.currentState = currentState;
			farmer.save();
		}
		return farmer;
	}
	
	public Farmer buyBase(Farmer farmer, Long itemid, String currentState) throws NotEnoughMoneyException{
		Field field = Field.find("owner.id", farmer.id).first();

		Plantation plantation = Plantation.buildInstance();
		plantation.base = Base.findById(itemid);

		field.plantation = plantation;

		farmer.currentState = currentState;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		moneyService.commitMoneyTransaction(farmer, -plantation.base.price);

		plantation.save();
		field.save();
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer buySeedling(Farmer farmer, Long seedlingTypeId,
			Long plantTypeId, String currentState)
			throws NotEnoughMoneyException {
		Plantation plantation = Plantation.find("field.owner.id", farmer.id)
				.first();

		plantation.seadlings = Seedling
				.find("type.id=:t and seedlingType.id=:st")
				.setParameter("t", plantTypeId)
				.setParameter("st", seedlingTypeId).first();

		farmer.currentState = currentState;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		Double value = plantation.seadlings.price * plantation.field.area;
		moneyService.commitMoneyTransaction(farmer, -value);

		plantation.save();
		farmer.save();
		return farmer;
	}

	@Override
	public HashMap<String, List<StoreItemDto>> storeItems() {
		HashMap<String, List<StoreItemDto>> result = new HashMap<String, List<StoreItemDto>>();
		result.put("tractor", tractorStoreItems());
		result.put("terrain", terrainStoreItems());
		result.put("terrain-size", terrainSizeItems());
		result.put("base",terrainBaseItems());
		result.put("apple-type", plantTypes());
		result.put("seedlings", plantTypes());
		result.put("seedling-type",seedlingTypes());
		result.put("irrigation",irrigationStoreItems());
		result.put("other", otherStoreItems());
		result.put("spraying", sprayingStoreItems());
		result.put("stores",allStores());
		result.put("fertilizer",fertilizerStoreItems());
		return result;
		
		
	}
	
	public List<StoreItemDto> tractorStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store  = Store.find("byName","tractor").first();
		List<Item> items = Item.find("byStore",store).fetch();
		for (Item item: items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.url = item.imageurl;
			storeDto.price = (double)item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> terrainStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<Terrain> terrains = Terrain.findAll();
		for (Terrain terrain : terrains) {
			StoreItemDto item = new StoreItemDto();
			item.id = terrain.id;
			item.name = terrain.description;
			item.url = terrain.imageurl;
			result.add(item);
		}
		return result;
	}
	
	public List<StoreItemDto> terrainSizeItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<TerrainSize> terrains = TerrainSize.findAll();
		for (TerrainSize terrainSize : terrains) {
			StoreItemDto item = new StoreItemDto();
			item.id = terrainSize.id;
			item.name = terrainSize.name;
			item.url = terrainSize.imageurl;
			item.size = terrainSize.size;
			item.price = terrainSize.price;
			result.add(item);
		}
		return result;
	}
	
	public List<StoreItemDto> terrainBaseItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<Base> bases = Base.findAll();
		for (Base base : bases) {
			StoreItemDto item = new StoreItemDto();
			item.id = base.id;
			item.name = base.name;
			item.price = (double)base.price;
			item.url = base.imageurl;
			result.add(item);
		}
		return result;
	}
	
	public List<StoreItemDto> plantTypes() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<PlantType> types = PlantType.findAll();
		for (PlantType plantType : types) {
			StoreItemDto item = new StoreItemDto();
			item.id = plantType.id;
			item.name = plantType.name;
			item.url = plantType.imageurl;
			result.add(item);
		}
		return result;
	}
	
	public List<StoreItemDto> seedlingTypes() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<SeedlingType> types = SeedlingType.findAll();
		for (SeedlingType seedlingType : types) {
			StoreItemDto item = new StoreItemDto();
			item.id = seedlingType.id;
			item.name = seedlingType.description;
			item.url = seedlingType.imageurl;
			result.add(item);
		}
		return result;
	}
	
	public List<StoreItemDto> irrigationStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store  = Store.find("byName","irrigation").first();
		List<Item> items = Item.find("byStore",store).fetch();
		for (Item item: items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.url = item.imageurl;
			storeDto.price = (double)item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> fertilizerStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store  = Store.find("byName","fertilizer").first();
		List<Item> items = Item.find("byStore",store).fetch();
		for (Item item: items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.url = item.imageurl;
			storeDto.price = (double)item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> sprayingStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store  = Store.find("byName","spraying").first();
		List<Item> items = Item.find("byStore",store).fetch();
		for (Item item: items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.url = item.imageurl;
			storeDto.price = (double)item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> otherStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store  = Store.find("byName","other").first();
		List<Item> items = Item.find("byStore",store).fetch();
		for (Item item: items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.url = item.imageurl;
			storeDto.price = (double)item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> allStores() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<Store> stores  = Store.findAll();
		for (Store store: stores) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.name = store.name;
			storeDto.url = store.imageurl;
			result.add(storeDto);
		}
		return result;
	}
	
}