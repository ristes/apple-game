package service.impl;

import java.util.Date;

import models.Disease;
import models.Farmer;
import models.LogFarmerData;
import models.Operation;
import service.LogFarmerDataService;
import service.ServiceInjector;

public class LogFarmerDataServiceImpl implements LogFarmerDataService{

	@Override
	public void logSetMaxYield(Farmer farmer,  double yield) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.typelog = LogFarmerDataService.MAX_YIELD;
		data.information = yield;
		data.save();
		
	}

	@Override
	public void logMoneySpent(Farmer farmer, Integer ammount) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.typelog = LogFarmerDataService.MONEY_SPENT;
		data.information = (double)ammount;
		data.save();
	}

	@Override
	public void logMoneyEarned(Farmer farmer, Integer ammount) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.logdate = farmer.gameDate.date;
		data.typelog = LogFarmerDataService.MONEY_EARNED;
		data.information = (double)ammount;
		data.save();
	}

	@Override
	public void logApplesBurned(Farmer farmer,Integer ammount) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.typelog = LogFarmerDataService.APPLES_BURNT;
		data.information = (double)ammount;
		data.save();
		
	}

	@Override
	public void logOccurredDisease(Farmer farmer, Disease disease,
			Integer applesBurned) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.typelog = LogFarmerDataService.DISEASES_OCCURED;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = applesBurned.doubleValue();
		data.disease = disease;
		data.save();
		
	}

	@Override
	public void logExecutedOperation(Farmer farmer, 
			Operation operation) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.operation = operation;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.typelog = LogFarmerDataService.OPERATION_EXECUTED;
		data.save();
	}

	@Override
	public void logApplesSold(Farmer farmer, Integer quantity) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = quantity.doubleValue();
		data.save();
	}

	@Override
	public void logApplesBurnedInFridge(Farmer farmer, Integer ammount) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = ammount.doubleValue();
		data.save();
		
	}

	
}
