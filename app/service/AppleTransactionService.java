package service;

import models.Farmer;
import exceptions.NotEnoughApplesException;

public interface AppleTransactionService {

	
	public Farmer commitAppleTransaction(Farmer farmer, Integer quantity) throws NotEnoughApplesException;
	public Farmer commitAppleToFarmerTransaction(Farmer farmerFrom, Farmer farmerTo, Integer quantity) throws NotEnoughApplesException;
}
