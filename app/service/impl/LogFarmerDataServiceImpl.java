package service.impl;

import java.util.Date;

import com.google.gson.Gson;

import dto.QuizResultsDto;
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
			Operation operation, Double information) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.operation = operation;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.typelog = LogFarmerDataService.OPERATION_EXECUTED;
		if (information!=null) {
			data.information = information;
		}
		data.save();
	}

	@Override
	public void logApplesSold(Farmer farmer, Integer quantity) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = quantity.doubleValue();
		data.typelog = LogFarmerDataService.APPLES_SOLD;
		data.save();
	}

	@Override
	public void logApplesBurnedInFridge(Farmer farmer, Integer ammount) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = ammount.doubleValue();
		data.typelog = LogFarmerDataService.APPLES_BURNED_IN_FRIDGE;
		data.save();
		
	}

	@Override
	public void logQuizAnswered(Farmer farmer, QuizResultsDto result) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.jsonDataInfo = new Gson().toJson(result);
		data.typelog = LogFarmerDataService.QUIZ_ANSWERED;
		data.save();
		
	}

	@Override
	public void logRainValue(Farmer farmer, Double value) {
		LogFarmerData data = new LogFarmerData();
		data.farmer = farmer;
		data.logdate = farmer.gameDate.date;
		data.recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		data.information = value;
		data.typelog = LogFarmerDataService.RAIN_VALUE;
		data.save();
	}
	
	 

	
}
