package service.impl;

import java.util.Calendar;
import java.util.Date;

import models.Farmer;
import models.PlantType;
import models.PlantationSeedling;
import models.Yield;
import service.HarvestService;
import service.ServiceInjector;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public class HarvestServiceImpl implements HarvestService {

	public Boolean isInHarvestingPeriod(Farmer farmer, PlantType plantType) {
		Date dateCurIn70s = ServiceInjector.dateService
				.convertDateTo70(farmer.gameDate.date);
		if (dateCurIn70s.after(plantType.period.startFrom)) {
			if (dateCurIn70s.before(plantType.period.endTo)) {
				return true;
			}
		}
		return false;
	}

	public Farmer makeHarvesting(Farmer farmer,
			PlantationSeedling plantationSeedling, Double goodper, Double badper)
			throws NotEnoughMoneyException, NotAllowedException {
		double expense = farmer.field.area * 3000;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer,
				-expense);
		Calendar cal = Calendar.getInstance();
		cal.setTime(farmer.gameDate.date);
		// int year = cal.get(Calendar.YEAR);
		int recolteYear = ServiceInjector.dateService.recolteYearByPlantType(
				farmer.gameDate.date, plantationSeedling.seedling.type);
		Yield yieldDone = Yield.find(
				"year=?1 And farmer=?2 And plantationSeedling=?3", recolteYear,
				farmer, plantationSeedling).first();
		if (yieldDone != null) {
			throw new NotAllowedException();
		}
		if (!isInHarvestingPeriod(farmer, plantationSeedling.seedling.type)) {
			throw new NotAllowedException();
		}
		int quantity = (int) (farmer.productQuantity
				* plantationSeedling.percentOfPlantedArea / 100.0);
		// scaling to decrease the looses harvesting bad apples
		badper = badper * 0.2;
		int q = (int) (quantity * goodper - quantity * badper);
		if (q < 0) {
			q = 0;
		}
		farmer.save();
		Yield yield = new Yield();
		yield.farmer = farmer;
		yield.plantationSeedling = plantationSeedling;
		yield.type = plantationSeedling.seedling.seedlingType;
		yield.quantity = q;
		yield.year = recolteYear;
		yield.save();
		return farmer;
	}

	@Override
	public Farmer makeShtarjfTest(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isInHarvestingPeriod(Farmer farmer, Long plantType) {
		PlantType plant = PlantType.findById(plantType);
		return isInHarvestingPeriod(farmer, plant);
	}

	@Override
	public Boolean isAfterHarvestingPeriod(Farmer farmer, PlantType plantType) {
		Date dateCurIn70s = ServiceInjector.dateService
				.convertDateTo70(farmer.gameDate.date);
		if (dateCurIn70s.after(plantType.period.endTo)) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean isAfterHarvestingPeriod(Farmer farmer, Long plantType) {
		PlantType plant = PlantType.findById(plantType);
		return isAfterHarvestingPeriod(farmer, plant);
	}

}
