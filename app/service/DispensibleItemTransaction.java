package service;

import exceptions.NotEnoughItemsException;
import models.Farmer;
import models.Item;

public interface DispensibleItemTransaction {
	
	public Farmer commitDispensibleItemTransaction(Farmer farmerFrom, Farmer farmerTo, Item item, double quantity) throws NotEnoughItemsException;

}
