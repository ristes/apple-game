package service;

import models.Farmer;
import models.PlantType;
import exceptions.PriceNotValidException;

public interface PriceService {

	public Double price(Farmer farmer) throws PriceNotValidException;
	public Double price(Farmer farmer, PlantType plantType) throws PriceNotValidException;
}
