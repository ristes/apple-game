package service;

import exceptions.PriceNotValidException;
import models.Farmer;

public interface PriceService {

	public Double price(Farmer farmer) throws PriceNotValidException;
}
