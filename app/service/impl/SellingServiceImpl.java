package service.impl;

import java.util.List;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import models.Yield;
import models.YieldPortion;
import models.YieldPortionSold;
import service.MoneyTransactionService;
import service.SellingService;
import service.ServiceInjector;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;

public class SellingServiceImpl implements SellingService {

	@Override
	public double sell(Farmer farmer, PlantType plantType, Integer quantity)
			throws PriceNotValidException, NotEnoughApplesException,
			NotEnoughMoneyException {

		int recolteYear = ServiceInjector.dateService
				.recolteYear(farmer.gameDate.date);
		Yield yield = Yield
				.find("year=?1 And farmer=?2 AND plantationSeedling.seedling.type =?3",
						recolteYear, farmer, plantType).first();
		if (yield==null) {
			yield = Yield
					.find("year=?1 And farmer=?2 AND plantationSeedling.seedling.type =?3",
							recolteYear-1, farmer, plantType).first();
		}
		if (yield != null) {
			yield.storedQuantity = 0;
			for (YieldPortion yp : yield.yieldPortions) {
				yield.storedQuantity += yp.quantity;
			}
			for (YieldPortionSold yp : yield.yieldPortionsSold) {
				yield.storedQuantity += yp.quantity;
			}
		} else {
			throw new NotEnoughApplesException();
		}
		if (yield.quantity - yield.storedQuantity < quantity) {
			throw new NotEnoughApplesException();
		}

		double price = ServiceInjector.priceService.price(farmer, plantType);
		double sum_money = price * quantity;
		MoneyTransactionService moneyTransaction = new TransactionServiceImpl();
		moneyTransaction.commitMoneyTransaction(farmer,
				ServiceInjector.moneyConversionService.toEuros(sum_money));
		ServiceInjector.logFarmerDataService.logApplesSold(farmer, quantity);
		YieldPortionSold sold = new YieldPortionSold();
		sold.yield = yield;
		sold.quantity = quantity;
		sold.price = sum_money;
		sold.date = farmer.gameDate.date;
		sold.save();
		return ServiceInjector.moneyConversionService.toEuros(sum_money);
	}

}
