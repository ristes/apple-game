package service;

import java.util.List;

import models.Farmer;
import models.PlantType;
import models.PlantationSeedling;
import dao.FridgeUsageDto;

public interface FridgeService {

	public static final Integer NO_FRIDGE = 0;
	public static final Integer NA_FRIDGE = 1;
	public static final Integer CA_FRIDGE = 2;

	public void setFridgesSeedlingType(Farmer farmer,
			List<PlantationSeedling> plantationSeedling);

	public void buyFridgeCapacity(Farmer farmer, int capacity, int fridgeType);

	public void addToFridge(Farmer farmer, PlantType type, int quantity);

	public void removeFromFridge(Farmer farmer, PlantType type, int quantity);

	public FridgeUsageDto getFridgeUsage(Farmer farmer, int fridgeType);

	public List<FridgeUsageDto> getFarmerFridges(Farmer farmer);

}
