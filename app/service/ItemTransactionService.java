package service;

import exceptions.NotEnoughMoneyException;
import models.Farmer;
import models.Item;

public interface ItemTransactionService {

	public Farmer commitBuyingItem(Farmer farmer, Item item, Double quantity) throws NotEnoughMoneyException;
}
