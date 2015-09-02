package service.impl;

import java.util.List;

import models.Farmer;
import models.PlantType;
import models.RottenApples;
import service.RottenApplesService;

public class RottenApplesServiceImpl implements RottenApplesService{

	@Override
	public List<RottenApples> getByFarmerYearAndPlantType(Farmer farmer,
			Integer year, PlantType plantType) {
		return RottenApples.find("farmer=?1 and year=?2 and plantType=?3", farmer,year,plantType).fetch();
	}

}
