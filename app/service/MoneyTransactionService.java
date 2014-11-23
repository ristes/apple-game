package service;

import models.Farmer;
import exceptions.NotEnoughMoneyException;

public interface MoneyTransactionService {
	public Farmer commitMoneyTransaction(Farmer farmer, double value) throws NotEnoughMoneyException;
	public Farmer commitMoneyTransactionFromOtherFarmer(Farmer farmerTo, Farmer farmerFrom, double value) throws NotEnoughMoneyException;
	public Farmer commitMoneyTransactionToOtherFarmer(Farmer farmerFrom, Farmer farmerTo, double value) throws NotEnoughMoneyException;
}
