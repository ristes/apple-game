package service;

import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import models.Yield;

public interface YieldService {

	public final static int START_YEAR = 2022;
	
	public Double calculateYield(Farmer farmer);
	
	public Double getMaxYieldByRecolte(Farmer farmer, Integer recolte) ;
	
	public List<Yield> getPreviousYearYield(Farmer farmer);
	
}
