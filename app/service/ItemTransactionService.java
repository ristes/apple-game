package service;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import exceptions.NotEnoughMoneyException;

public interface ItemTransactionService {

	public ItemInstance commitBuyingItem(Farmer farmer, Item item, Double quantity) throws NotEnoughMoneyException;
}
