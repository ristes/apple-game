package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.Field;
import models.PlantType;
import models.PlantationSeedling;
import service.PlantTypeService;

public class PlantTypeServiceImpl implements PlantTypeService{

	@Override
	public List<PlantType> ownedByFarmer(Farmer farmer) {
		List<PlantType> result = new ArrayList<PlantType>();
		for (PlantationSeedling ps : farmer.field.plantation.seedlings) {
			result.add(ps.seedling.type);
		}
		return result;
	}

}
