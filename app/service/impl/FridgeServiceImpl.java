package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.Fridge;
import models.FridgeSeedlingType;
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

}
