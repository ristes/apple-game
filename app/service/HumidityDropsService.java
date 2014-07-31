package service;

import models.Farmer;

public interface HumidityDropsService {

	public int drops_irrigation_delta_impact_quantity(Farmer farmer);
	public double drops_irrigation_delta_impact_eco_point(Farmer farmer);
	public double varianceDrops(Farmer farmer);
}
