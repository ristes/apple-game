package service;

import models.Farmer;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public interface HarvestService {
	public Farmer makeShtarjfTest(Farmer farmer);
	public Farmer makeHarvesting(Farmer farmer, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException;
}
