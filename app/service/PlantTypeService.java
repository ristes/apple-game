package service;

import java.util.List;

import models.Farmer;
import models.PlantType;

public interface PlantTypeService {

	
	public List<PlantType> ownedByFarmer(Farmer farmer);
}
