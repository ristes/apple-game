package service.impl;

import java.util.Calendar;
import java.util.Date;

import models.Farmer;
import models.Fridge;
import models.PlantTypeDeadine;
import models.YieldPortion;
import service.ServiceInjector;
import service.YieldPortionService;

public class YieldPortionServiceImpl implements YieldPortionService {

	@Override
	public Integer removeFromPortion(YieldPortion yieldportion, int quantity) {
		yieldportion.quantity -= quantity;
		if (yieldportion.quantity < 0) {
			return Math.abs(yieldportion.quantity);
		}
		return 0;
	}

	@Override
	public Boolean checkDeadline(Farmer farmer, Fridge fridge,
			YieldPortion portion) {
		PlantTypeDeadine deadline = PlantTypeDeadine.find(
				"byPlantTypeAndFridge",
				portion.yield.plantationSeedling.seedling.type, fridge).first();
		Calendar cal = Calendar.getInstance();
		cal.setTime(farmer.gameDate.date);
		int monthToday = cal.get(Calendar.MONTH);
		int dayToday = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(deadline.date);
		int monthDeadline = cal.get(Calendar.MONTH);
		int dayDeadline = cal.get(Calendar.DAY_OF_MONTH);
		if (monthDeadline == monthToday && dayDeadline == dayToday) {
			return true;
		}
		return false;
	}

}
