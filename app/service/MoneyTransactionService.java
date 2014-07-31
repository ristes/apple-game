package service;

import exceptions.NotEnoughMoneyException;
import models.Farmer;

public interface MoneyTransactionService {
	public Farmer commitMoneyTransaction(Farmer farmer, double value) throws NotEnoughMoneyException;
	public Farmer commitMoneyTransactionFromOtherFarmer(Farmer farmerTo, Farmer farmerFrom, double value) throws NotEnoughMoneyException;
	public Farmer commitMoneyTransactionToOtherFarmer(Farmer farmerFrom, Farmer farmerTo, double value) throws NotEnoughMoneyException;
}
