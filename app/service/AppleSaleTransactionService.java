package service;

import models.Farmer;
import exceptions.NotEnoughApplesException;

public interface AppleSaleTransactionService {

	public Farmer commitAppleSaleTransaction(Farmer farmer, Integer quantity) throws NotEnoughApplesException;
	public Farmer commitAppleSaleToFarmerTransaction(Farmer farmerFrom, Farmer farmerTo, Integer quantity) throws NotEnoughApplesException;
}
