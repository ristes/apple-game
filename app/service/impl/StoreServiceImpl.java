package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.Base;
import models.Farmer;
import models.Field;
import models.Item;
import models.PlantType;
import models.Plantation;
import models.PlantationSeedling;
import models.SeedlingType;
import models.Store;
import models.Terrain;
import models.TerrainSize;
import service.ServiceInjector;
import service.StoreService;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import dao.DaoInjector;
import dto.StatusDto;
import dto.StoreDto;
import dto.StoreItemDto;
import exceptions.NotEnoughMoneyException;

public class StoreServiceImpl implements StoreService {

	@Override
	public List<StoreDto> findAllStores() {
		List<StoreDto> result = new ArrayList<StoreDto>();
		List<Store> stores = Store.findAll();
		for (Store store : stores) {
			StoreDto dto = new StoreDto();
			dto.id = store.id;
			dto.name = store.name;
			dto.description = store.description;
			dto.url = store.imageurl;
			result.add(dto);
		}
		return result;
	}

	public StatusDto buyItem(Farmer farmer, String itemName, Double quantity,
			String currentState) {
		StatusDto statusRes = new StatusDto(true,null,null,farmer, null);
		if (farmer != null) {
			Item item = Item.find("name", itemName).first();
			try {
				farmer = ServiceInjector.itemTransactionService.commitBuyingItem(farmer, item, quantity);
			} catch (NotEnoughMoneyException ex) {
				ex.printStackTrace();
			}

			if (currentState != null) {
				farmer.currentState = currentState;
			}
			checkMetaData(statusRes, item);
			statusRes.farmer.save();
		}
		return statusRes;
	}

	private void checkMetaData(StatusDto status, Item item) {
		if (item.metadata == null) {
			return;
		}
		
		JsonParser jsonParser = new JsonParser();
		JsonElement element = jsonParser.parse(item.metadata);
		JsonElement jsonEco = element.getAsJsonObject().get("eco");
		JsonElement jsonMoreNAStorage = element.getAsJsonObject().get("moreNAStorage");
		JsonElement jsonMoreCAStorage = element.getAsJsonObject().get("moreCAStorage");
		JsonElement jsonYield = element.getAsJsonObject().get("yield");
		JsonElement jsonTips = element.getAsJsonObject().get("tips");
		
		if (jsonMoreCAStorage!=null) {
			status.farmer.capacityCAFridges+=jsonMoreCAStorage.getAsInt();
		}
		if (jsonMoreNAStorage!=null) {
			status.farmer.capacityNAFridges += jsonMoreNAStorage.getAsInt();
		}
		if (jsonYield!=null) {
			status.farmer.productQuantity += status.farmer.productQuantity * jsonYield.getAsInt()/100;
		}
		if (jsonEco!=null) {
			int eco = jsonEco.getAsInt();
			if (eco<0) {
				ServiceInjector.ecoPointsService.substract(status.farmer, Math.abs(eco));
			} else {
				ServiceInjector.ecoPointsService.add(status.farmer, eco);
			}
		}
		
		if (jsonTips != null) {
			status.tip = jsonTips.getAsString();
		}

	}

	public Plantation getOrCreatePlantation(Farmer farmer) {
		Plantation plantation = null;
		try {
			plantation = Plantation.find("field.owner.id", farmer.id).first();
		} catch (Exception ex) {
			// return createPlantation(farmer);
		}
		if (plantation == null) {
			return createPlantation(farmer);
		} else {
			return plantation;
		}

	}

	public Plantation createPlantation(Farmer farmer) {
		Field field = Field.find("owner.id", farmer.id).first();
		Plantation plantation = ServiceInjector.plantationService.buildInstance();
		field.plantation = plantation;
		plantation.field = field;
		plantation.save();
		field.save();
		return plantation;
	}

	public Farmer buyBase(Farmer farmer, Long itemid, String currentState)
			throws NotEnoughMoneyException {
		Field field = Field.find("owner.id", farmer.id).first();

		Plantation plantation = getOrCreatePlantation(farmer);
		plantation.base = Base.findById(itemid);

		field.plantation = plantation;

		farmer.currentState = currentState;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -plantation.base.price);

		plantation.save();
		field.save();
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer buySeedling(Farmer farmer, List<PlantationSeedling> seedling,
			String currentState) throws NotEnoughMoneyException {
		Plantation plantation = getOrCreatePlantation(farmer);
		plantation.field = farmer.field;
		Double value = 0d;
		Integer numSeedlings = 0;
		for (PlantationSeedling ps : seedling) {
			value += ps.seedling.price * ps.quantity * plantation.field.area;
			numSeedlings += ps.quantity;
		}
		plantation.currentQuantity = numSeedlings;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -value);

		farmer.currentState = currentState;

		plantation.save();
		for (PlantationSeedling ps : seedling) {
			ps.plantation = plantation;
			ps.save();
		}
		farmer.save();
		return farmer;
	}

	@Override
	public HashMap<String, List<StoreItemDto>> storeItems(Farmer farmer) {
		HashMap<String, List<StoreItemDto>> result = new HashMap<String, List<StoreItemDto>>();
		result.put("tractor", tractorStoreItems());
		result.put("terrain", terrainStoreItems());
		result.put("terrain-size", terrainSizeItems());
		result.put("base", terrainBaseItems());
		result.put("apple-type", plantTypes());
		result.put("seedlings", plantTypes());
		result.put("seedling-type", seedlingTypes());
		result.put("irrigation", irrigationStoreItems());
		result.put("other", otherUnboughtStoreItems(farmer));
		result.put("spraying", sprayingStoreItems());
		result.put("stores", allStores());
		result.put("fertilizer", fertilizerStoreItems());
		result.put("digging", diggingStoreItems());
		return result;

	}
	
	public List<StoreItemDto> otherUnboughtStoreItems(Farmer farmer) {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<Item> items = DaoInjector.itemsDao.getUnboughtItem(farmer);
		List<StoreItemDto> resultUnbought = new ArrayList<StoreItemDto>();
		for (Item item : items) {
			StoreItemDto storeDto = toStoreItemDto(item);
			resultUnbought.add(storeDto);
		}
		return resultUnbought;
	}

	public List<StoreItemDto> tractorStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "tractor").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			storeDto.perHa = item.perHa;
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
			item.name = terrain.name;
			item.description = terrain.description;
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
			item.description = terrainSize.description;
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
			item.description = base.description;
			item.price = (double) base.price;
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
			item.description = plantType.description;
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
			item.name = seedlingType.name;
			item.description = seedlingType.description;
			item.url = seedlingType.imageurl;
			result.add(item);
		}
		return result;
	}

	public List<StoreItemDto> irrigationStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "irrigation").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}

	public List<StoreItemDto> fertilizerStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "fertilizer").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}
	
	public List<StoreItemDto> diggingStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "digging").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}

	public List<StoreItemDto> sprayingStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "spraying").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		return result;
	}

	public List<StoreItemDto> otherStoreItems() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		Store store = Store.find("byName", "other").first();
		List<Item> items = Item.find("byStore", store).fetch();
		for (Item item : items) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.id = item.id;
			storeDto.name = item.name;
			storeDto.description = item.description;
			storeDto.url = item.imageurl;
			storeDto.price = (double) item.price;
			storeDto.store = item.store.name;
			result.add(storeDto);
		}
		
		return result;
	}

	public List<StoreItemDto> allStores() {
		List<StoreItemDto> result = new ArrayList<StoreItemDto>();
		List<Store> stores = Store.findAll();
		for (Store store : stores) {
			StoreItemDto storeDto = new StoreItemDto();
			storeDto.name = store.name;
			storeDto.description = store.description;
			storeDto.url = store.imageurl;
			result.add(storeDto);
		}
		return result;
	}

	
	public StoreItemDto toStoreItemDto(Item item) {
		StoreItemDto storeDto = new StoreItemDto();
		storeDto.id = item.id;
		storeDto.name = item.name;
		storeDto.description = item.description;
		storeDto.url = item.imageurl;
		storeDto.price = (double) item.price;
		storeDto.store = item.store.name;
		return storeDto;
	}

}
