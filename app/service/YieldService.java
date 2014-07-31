package service;

import models.Farmer;

public interface YieldService {

	public final static int START_YEAR = 2022;
	
	public Double calculateYield(Farmer farmer);
	
}
