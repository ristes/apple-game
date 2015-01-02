package service;

import models.Farmer;

public interface HumidityGroovesService {
	
	public void calculateGroovesVarianceImpact(Farmer farmer);
	public double varianceBrazdi(Farmer farmer);

}
