package service.impl;

import java.util.ArrayList;
import java.util.List;

import dao.FridgeUsageDto;
import models.Farmer;
import models.Fridge;
import models.FridgeSeedlingType;
import models.PlantType;
import models.PlantationSeedling;
import service.FridgeService;

public class FridgeServiceImpl implements FridgeService{

	@Override
	public void setFridgesSeedlingType(Farmer farmer, 
			List<PlantationSeedling> plantationSeedlings) {
		List<FridgeSeedlingType> fridgeForSeedling = new ArrayList<FridgeSeedlingType>();
		
		Fridge naFridge = new Fridge();
		naFridge.capacity = 0;
		naFridge.type = FridgeService.NA_FRIDGE;
		naFridge.farmer = farmer;
		naFridge.save();
		
		Fridge caFridge = new Fridge();
		caFridge.capacity = 0;
		caFridge.type = FridgeService.NA_FRIDGE;
		caFridge.farmer = farmer;
		caFridge.save();
		
		for (PlantationSeedling ps: plantationSeedlings) {
			FridgeSeedlingType nafst = new FridgeSeedlingType();
			
			nafst.current_state = 0l;
			nafst.fridge = naFridge;
			nafst.plantType = ps.seedling.type;
			nafst.save();
			
			FridgeSeedlingType cafst = new FridgeSeedlingType();
			cafst.current_state = 0l;
			cafst.fridge = caFridge;
			cafst.plantType = ps.seedling.type;
			cafst.save();
			
		}
		
		
		
	}

	@Override
	public void buyFridgeCapacity(Farmer farmer, int capacity, int fridgeType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addToFridge(Farmer farmer, PlantType type, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromFridge(Farmer farmer, PlantType type, int quantity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FridgeUsageDto getFridgeUsage(Farmer farmer, int fridgeType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FridgeUsageDto> getFarmerFridges(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

}
