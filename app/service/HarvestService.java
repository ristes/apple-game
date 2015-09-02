package service;

import models.Farmer;
import models.PlantType;
import models.PlantationSeedling;
import exceptions.InvalidYield;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public interface HarvestService {
	
	public static final Integer PRIZE = 50;
	
	public Boolean isAfterHarvestingPeriod(Farmer farmer, PlantType plantType);
	public Boolean isAfterHarvestingPeriod(Farmer farmer, Long plantType);
	public Boolean isInHarvestingPeriod(Farmer farmer, PlantType plantType);
	public Boolean isInHarvestingPeriod(Farmer farmer, Long plantType);
	public Farmer makeShtarjfTest(Farmer farmer);
	public Farmer makeHarvesting(Farmer farmer, PlantationSeedling plantationseedling, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException , NotEnoughSpaceInFridge, InvalidYield;
	public Boolean isInGlobalHarvetingPeriod(Farmer farmer);
}
