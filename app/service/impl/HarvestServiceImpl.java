package service.impl;

import java.util.Calendar;

import models.Farmer;
import models.PlantationSeedling;
import models.Yield;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import service.HarvestService;
import service.MoneyTransactionService;
import service.ServiceInjector;

public class HarvestServiceImpl implements HarvestService {

	public Farmer makeHarvesting(Farmer farmer, PlantationSeedling plantationSeedling, Double goodper, Double badper) throws NotEnoughMoneyException, NotAllowedException{
		double expense = farmer.field.area * 3000;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -expense);
		Calendar cal = Calendar.getInstance();
		cal.setTime(farmer.gameDate.date);
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		Yield yieldDone = Yield.find("byYearAndFarmerAndPlantation", year, farmer, plantationSeedling).first();
		if (yieldDone != null) {
			throw new NotAllowedException();
		}
		if (month != 8) {
			throw new NotAllowedException();
		}
		int quantity = (int)(farmer.productQuantity * plantationSeedling.percentOfPlantedArea/100.0);
		//scaling to decrease the looses harvesting bad apples
		badper = badper*0.2;
		int q = (int)(quantity*goodper-quantity*badper);
		if (q < 0) {
			q = 0;
		}
		farmer.save();
		Yield yield = new Yield();
		yield.farmer = farmer;
		yield.plantation = plantationSeedling;
		yield.quantity = q;
		yield.year = year;
		yield.save();
		return farmer;
	}

	@Override
	public Farmer makeShtarjfTest(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

}
