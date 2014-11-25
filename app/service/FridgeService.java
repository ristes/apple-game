package service;

import java.util.List;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import models.PlantationSeedling;
import models.Yield;
import models.YieldPortion;
import dao.FridgeUsageDto;
import exceptions.InvalidYield;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public interface FridgeService {

	public static final Integer NO_FRIDGE = 0;
	public static final Integer NA_FRIDGE = 1;
	public static final Integer CA_FRIDGE = 2;

	public Fridge getFridge(Farmer farmer, Integer type);

	public void setFridges(Farmer farmer,
			List<PlantationSeedling> plantationSeedling);

	public void buyFridgeCapacity(Farmer farmer, int capacity, int fridgeType)
			throws NotEnoughMoneyException;

	public void addToFridge(Farmer farmer, Fridge fridge, PlantType type,
			int quantity) throws NotEnoughSpaceInFridge, InvalidYield;

	public void removeFromFridge(Farmer farmer, Fridge fridge, PlantType type,
			int quantity) throws NotEnoughApplesException;
	

	public FridgeUsageDto getFridgeUsage(Farmer farmer, int fridgeType);

	public List<FridgeUsageDto> getFarmerFridges(Farmer farmer);

	public Integer getTotalApplesInStock(Farmer farmer);
	
	public void checkApplesState(Farmer farmer);
	
	public void checkApplesStateForFridge(Farmer farmer, Fridge fridge, List<Yield> yields);
	
	public Boolean deadlineApples(Farmer farmer, Fridge fridge, YieldPortion portion);

}
