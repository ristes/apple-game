package service;

import models.Farmer;

public interface HumidityDropsService {

	public double varianceDrops(Farmer farmer);
	public void calculateDropsVarianceImpact(Farmer farmer);
}
