package service.impl;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import service.MoneyTransactionService;
import service.SellingService;
import service.ServiceInjector;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;

public class SellingServiceImpl implements SellingService{

	@Override
	public double sell(Farmer farmer, Fridge fridge, PlantType plantType,
			Integer quantity) throws PriceNotValidException, NotEnoughApplesException, NotEnoughMoneyException{
		
		
		ServiceInjector.fridgeService.removeFromFridge(farmer, fridge, plantType, quantity);
		double price = ServiceInjector.priceService.price(farmer, plantType);
		double sum_money = price * quantity;
		MoneyTransactionService moneyTransaction = new TransactionServiceImpl();
		moneyTransaction.commitMoneyTransaction(farmer, ServiceInjector.moneyConversionService.toEuros(sum_money));
		ServiceInjector.logFarmerDataService.logApplesSold(farmer, quantity);
		return sum_money;
		
	}

}
