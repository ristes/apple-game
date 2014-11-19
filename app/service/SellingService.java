package service;

import models.Farmer;
import models.PlantType;

public interface SellingService {

	public void sell(Farmer farmer, PlantType plantType, int fridgeType,
			int quantity);
}
