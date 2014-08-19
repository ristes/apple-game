package service;

import java.util.HashMap;
import java.util.List;

import models.Farmer;
import dto.StoreDto;
import dto.StoreItemDto;
import exceptions.NotEnoughMoneyException;

public interface StoreService {

	public List<StoreDto> findAllStores();
	public Farmer buyItem(Farmer farmer, String itemName, Double quantity,
			String currentState);
	public Farmer buyBase(Farmer farmer, Long itemid, String currentState) throws NotEnoughMoneyException;
	public Farmer buySeedling(Farmer farmer, Long seedlingTypeId, Long plantTypeId,
			String currentState) throws NotEnoughMoneyException;
	public HashMap<String, List<StoreItemDto>> storeItems();
}

