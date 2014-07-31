package service;

import models.Farmer;

public interface HumidityGroovesService {
	
	public int grooves_irrigation_delta_impact_quantity(Farmer farmer);
	public double grooves_irrigation_delta_impact_eco_point(Farmer farmer);
	public double varianceBrazdi(Farmer farmer);

}
