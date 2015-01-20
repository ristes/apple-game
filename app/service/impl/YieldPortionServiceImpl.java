package service.impl;

import java.util.Calendar;
import java.util.Date;

import models.Farmer;
import models.Fridge;
import models.FridgeType;
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
	public Boolean checkDeadline(Farmer farmer, FridgeType fridge,
			YieldPortion portion) {
		PlantTypeDeadine deadline = PlantTypeDeadine.find(
				"plantType=?1 AND fridge=?2",
				portion.yield.plantType, fridge).first();
		Calendar cal = Calendar.getInstance();
		cal.setTime(ServiceInjector.dateService.convertFridgeDateTo70(farmer.gameDate.date));
		int yearToday = cal.get(Calendar.YEAR);
		int monthToday = cal.get(Calendar.MONTH);
		int dayToday = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(deadline.date);
		int yearDeadline = cal.get(Calendar.YEAR);
		int monthDeadline = cal.get(Calendar.MONTH);
		int dayDeadline = cal.get(Calendar.DAY_OF_MONTH);
		if (yearDeadline<yearToday) {
			return true;
		}
		if (yearDeadline>yearToday) {
			return false;
		}
		if (yearDeadline==yearToday && monthDeadline <= monthToday && dayDeadline < dayToday) {
			return true;
		}
		return false;
	}

}
