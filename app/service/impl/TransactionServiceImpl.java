package service.impl;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import play.i18n.Messages;
import service.AppleSaleTransactionService;
import service.AppleTransactionService;
import service.DateService;
import service.DispensibleItemTransaction;
import service.IndispensibleItemTransaction;
import service.InsuranceService;
import service.ItemTransactionService;
import service.MoneyTransactionService;
import service.ServiceInjector;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughItemsException;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;

public class TransactionServiceImpl implements MoneyTransactionService,
		ItemTransactionService, IndispensibleItemTransaction,
		DispensibleItemTransaction, AppleTransactionService,
		AppleSaleTransactionService {

	@Override
	public Farmer commitMoneyTransaction(Farmer farmer, double value)
			throws NotEnoughMoneyException {
		// expense
		if (value < 0.0) {
			value = Math.abs(value);
			if (value > farmer.getBalance()) {
				throw new NotEnoughMoneyException();
			}
			value = -value;
		}
		farmer.setBalance(farmer.getBalance() + value);
		farmer.save();
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
			throw new NotEnoughApplesException(
					Messages.get("controller.sale.fail"));
		}
		farmer.productQuantity -= quantity;
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
			throws NotEnoughApplesException, PriceNotValidException {
		try {
			farmer = commitAppleTransaction(farmer, quantity);
		} catch (NotEnoughApplesException ex) {
			throw ex;
		}
		try {
			Double price = ServiceInjector.priceService.price(farmer);
			farmer.setBalance(farmer.getBalance() + quantity * price);
		} catch (PriceNotValidException ex) {
			throw ex;
		}
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
	public Farmer commitBuyingItem(Farmer farmer, Item item, Double quantity)
			throws NotEnoughMoneyException {
		if (item.price == 0) {
			commitBuyingSpecialItem(farmer, item, quantity);
		} else {
			DateService dateService = new DateServiceImpl();
			double value = item.price * quantity;
			commitMoneyTransaction(farmer, -value);
			ItemInstance instance = new ItemInstance();
			instance.ownedBy = farmer;
			instance.type = item;
			instance.quantity = quantity;
			instance.year = dateService.recolteYear(farmer.gameDate.date);
			instance.save();

			farmer.save();
		}
		return farmer;
	}

	public Farmer commitBuyingSpecialItem(Farmer farmer, Item item,
			Double quantity) throws NotEnoughMoneyException {
		if (item.name.equals("insurrance")) {
			farmer = ServiceInjector.insuranceService.buyInsurance(farmer);
		}
		return farmer;
	}

}
