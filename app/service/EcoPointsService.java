package service;

import models.Farmer;

public interface EcoPointsService {
	
	public static final Integer ECO_START_VALUE = 100;
	
	public void add(Farmer farmer, Integer value);
	public void substract(Farmer farmer, Integer value);
	public void divide(Farmer farmer, Double value);
	public void restart(Farmer farmer);

}