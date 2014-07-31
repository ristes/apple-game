package service.impl;

import java.util.Calendar;

import models.Farmer;
import models.Yield;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import service.HarvestService;
import service.MoneyTransactionService;

public class HarvestServiceImpl implements HarvestService {

	public Farmer makeHarvesting(Farmer farmer) throws NotEnoughMoneyException, NotAllowedException{
		double expense = farmer.field.area * 3000;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		moneyService.commitMoneyTransaction(farmer, -expense);
		Calendar cal = Calendar.getInstance();
		cal.setTime(farmer.gameDate.date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		Yield yieldDone = Yield.find("byYearAndFarmer", year, farmer).first();
		if (yieldDone != null) {
			throw new NotAllowedException();
		}
		if (month != 8) {
			throw new NotAllowedException();
		}
		int q = farmer.productQuantity;
		farmer.apples_in_stock += q;
		farmer.productQuantity = 0;
		farmer.save();
		Yield yield = new Yield();
		yield.farmer = farmer;
		yield.quantity = q;
		yield.year = year;
		yield.save();
		return farmer;
	}

}
