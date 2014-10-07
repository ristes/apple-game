package service.impl;

import java.util.Calendar;

import models.Farmer;
import models.Yield;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import service.HarvestService;
import service.MoneyTransactionService;

public class HarvestServiceImpl implements HarvestService {

	public Farmer makeHarvesting(Farmer farmer, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException{
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
		//scaling to decrease the looses harvesting bad apples
		badper = badper*0.2;
		int q = (int)(farmer.productQuantity*goodper-farmer.productQuantity*badper);
		if (q < 0) {
			q = 0;
		}
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
