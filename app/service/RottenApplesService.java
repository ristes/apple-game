package service;

import java.util.List;

import models.Farmer;
import models.PlantType;
import models.RottenApples;

public interface RottenApplesService {
	
	public List<RottenApples> getByFarmerYearAndPlantType(Farmer farmer, Integer year, PlantType plantType);

}
