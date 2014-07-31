package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import play.i18n.Messages;
import dto.C;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughItemsException;
import exceptions.NotEnoughMoneyException;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import service.AppleSaleTransactionService;
import service.AppleTransactionService;
import service.DispensibleItemTransaction;
import service.IndispensibleItemTransaction;
import service.ItemTransactionService;
import service.MoneyTransactionService;

public class TransactionServiceImpl implements MoneyTransactionService, ItemTransactionService, IndispensibleItemTransaction, DispensibleItemTransaction, AppleTransactionService, AppleSaleTransactionService{

	@Override
	public Farmer commitMoneyTransaction(Farmer farmer, double value) throws NotEnoughMoneyException{
		//expense
		if (value < 0.0) {
			value = Math.abs(value);
			if (value > farmer.getBalance()) {
				throw new NotEnoughMoneyException();
			}
			value = - value;
		}
		farmer.setBalance(farmer.getBalance() + value);
		
		return farmer;
	}

	@Override
	public Farmer commitDispensibleItemTransaction(Farmer farmerFrom,
			Farmer farmerTo, Item item, double quantity)
			throws NotEnoughItemsException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Farmer commitMoneyTransactionFromOtherFarmer(Farmer farmerTo,
			Farmer farmerFrom, double value) throws NotEnoughMoneyException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Farmer commitMoneyTransactionToOtherFarmer(Farmer farmerFrom,
			Farmer farmerTo, double value) throws NotEnoughMoneyException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Farmer commitAppleTransaction(Farmer farmer, Integer quantity)
			throws NotEnoughApplesException {
		if (farmer.productQuantity < quantity) {
			throw new NotEnoughApplesException(Messages.get("controller.sale.fail"));
		}
		farmer.productQuantity -= quantity;
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer commitAppleToFarmerTransaction(Farmer farmerFrom,
			Farmer farmerTo, Integer quantity) throws NotEnoughApplesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Farmer commitAppleSaleTransaction(Farmer farmer, Integer quantity)
			throws NotEnoughApplesException {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl.load_hash(C.COEF_SALES_YML);
		try {
			farmer = commitAppleTransaction(farmer, quantity);
		} catch (NotEnoughApplesException ex) {
			throw ex;
		}
		Calendar c = Calendar.getInstance();
		Date gameDate = farmer.gameDate.date;
		c.setTime(gameDate);
		double cena = coefs.get(C.KEY_SALE_PRICES).get(
				c.get(Calendar.MONTH));
		farmer.setBalance(farmer.getBalance()+quantity * cena);
		
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer commitAppleSaleToFarmerTransaction(Farmer farmerFrom,
			Farmer farmerTo, Integer quantity) throws NotEnoughApplesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Farmer commitBuyingItem(Farmer farmer, Item item, Double quantity) throws NotEnoughMoneyException{
		double value = item.price * quantity;
		commitMoneyTransaction(farmer, -value);
		ItemInstance instance = new ItemInstance();
		instance.ownedBy = farmer;
		instance.type = item;
		instance.quantity = quantity;
		instance.save();

		farmer.save();
		return farmer;
	}

	
	
	

}
