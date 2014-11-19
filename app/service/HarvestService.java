package service;

import models.Farmer;
import models.PlantationSeedling;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public interface HarvestService {
	public Farmer makeShtarjfTest(Farmer farmer);
	public Farmer makeHarvesting(Farmer farmer, PlantationSeedling plantationseedling, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException;
}
