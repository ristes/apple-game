package service;

import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;
import models.Farmer;
import models.Fridge;
import models.PlantType;

public interface SellingService {

	public double sell(Farmer farmer, PlantType plantType, Integer quantity)
			throws PriceNotValidException, NotEnoughApplesException,
			NotEnoughMoneyException;
}
