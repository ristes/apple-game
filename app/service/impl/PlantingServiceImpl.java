package service.impl;

import models.Farmer;
import service.PlantingService;

public class PlantingServiceImpl implements PlantingService{

	@Override
	public Farmer savePlantingParams(Farmer farmer,String array, Integer seedlings) {
		int optimum = 2380;
		switch (farmer.field.plantation.base.id.intValue()) {
		case 1:
			optimum = 1435;
			break;
		case 2:
			optimum = 1000;
			break;
		case 3:
		default:
			optimum = 1000;
			break;
		}
		farmer.field.plantation.fieldPercentage = (int) (seedlings * 35 * 100.0 / optimum);
		farmer.field.plantation.treePositions = array;
		farmer.field.plantation.save();
		return farmer;
	}

}
