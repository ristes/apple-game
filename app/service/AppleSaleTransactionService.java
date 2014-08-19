package service;

import models.Farmer;
import exceptions.NotEnoughApplesException;
import exceptions.PriceNotValidException;

public interface AppleSaleTransactionService {

	public Farmer commitAppleSaleTransaction(Farmer farmer, Integer quantity) throws NotEnoughApplesException,PriceNotValidException;
	public Farmer commitAppleSaleToFarmerTransaction(Farmer farmerFrom, Farmer farmerTo, Integer quantity) throws NotEnoughApplesException;
}
