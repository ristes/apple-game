package service.impl;

import models.Farmer;

public class GrassGrowthServiceImpl implements GrassGrowthService{

	@Override
	public void inc(Farmer farmer) {
		farmer.grass_growth += GRASS_GROWTH;
	}

}
