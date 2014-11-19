package service;

import java.util.List;

import models.Farmer;
import models.PlantationSeedling;

public interface FridgeService {
	
	public static final Integer NA_FRIDGE = 0;
	public static final Integer CA_FRIDGE = 0;
	
	public void setFridgesSeedlingType(Farmer farmer, List<PlantationSeedling> plantationSeedling);

}
