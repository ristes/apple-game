package service;

import models.Farmer;
import models.PlantType;
import models.PlantationSeedling;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public interface HarvestService {
	
	public static final Integer PRIZE = 50;
	
	public Boolean isAfterHarvestingPeriod(Farmer farmer, PlantType plantType);
	public Boolean isAfterHarvestingPeriod(Farmer farmer, Long plantType);
	public Boolean isInHarvestingPeriod(Farmer farmer, PlantType plantType);
	public Boolean isInHarvestingPeriod(Farmer farmer, Long plantType);
	public Farmer makeShtarjfTest(Farmer farmer);
	public Farmer makeHarvesting(Farmer farmer, PlantationSeedling plantationseedling, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException;
}
