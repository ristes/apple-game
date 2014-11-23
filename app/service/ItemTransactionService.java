package service;

import models.Farmer;
import models.Item;
import exceptions.NotEnoughMoneyException;

public interface ItemTransactionService {

	public Farmer commitBuyingItem(Farmer farmer, Item item, Double quantity) throws NotEnoughMoneyException;
}
