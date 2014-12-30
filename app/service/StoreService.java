package service;

import java.util.HashMap;
import java.util.List;

import models.Farmer;
import models.Item;
import models.Plantation;
import models.PlantationSeedling;
import dto.StatusDto;
import dto.StoreDto;
import dto.StoreItemDto;
import exceptions.NotEnoughMoneyException;

public interface StoreService {

	public List<StoreDto> findAllStores();

	public StatusDto buyItem(Farmer farmer, String itemName, Double quantity,
			String currentState);

	public Farmer buyBase(Farmer farmer, Long itemid, String currentState)
			throws NotEnoughMoneyException;

	public Farmer buySeedling(Farmer farmer, List<PlantationSeedling> seedling,
			String currentState) throws NotEnoughMoneyException;

	public HashMap<String, List<StoreItemDto>> storeItems(Farmer farmer);

	Plantation getOrCreatePlantation(Farmer farmer);

	Plantation createPlantation(Farmer farmer);
	
	public StoreItemDto toStoreItemDto(Item item);
}
