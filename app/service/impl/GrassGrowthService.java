package service.impl;

import models.Farmer;

public interface GrassGrowthService {
	
	public final static Double GRASS_GROWTH = 0.2;
	
	public void inc(Farmer farmer);

}
