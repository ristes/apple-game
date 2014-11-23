package service;

import models.Farmer;
import exceptions.PriceNotValidException;

public interface PriceService {

	public Double price(Farmer farmer) throws PriceNotValidException;
}
