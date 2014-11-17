package service.impl;

import java.util.Calendar;

import models.Farmer;
import models.ItemInstance;
import service.InfoTableService;
import service.RandomGeneratorService;
import service.ServiceInjector;
import service.ThiefService;

public class ThiefServiceImpl implements ThiefService {

	@Override
	public void checkThiefProb(Farmer farmer) {
		Calendar nowDate = Calendar.getInstance();
		nowDate.setTime(farmer.gameDate.date);
		int month = nowDate.get(Calendar.MONTH);
		if (month == Calendar.AUGUST || month == Calendar.SEPTEMBER
				|| month == Calendar.OCTOBER) {
			if (!hasProtectingNet(farmer)) {
				randomDemage(farmer);
			}

		}

	}

	public void randomDemage(Farmer farmer) {
		Long timeNotLogged = 0l;
		if (farmer.lastLogIn!=null) {
			timeNotLogged = System.currentTimeMillis()
					- farmer.lastLogIn.getTime();
		}
		
		long days = timeNotLogged / 1000 / 60 / 60 / 24;
		// modulus is for normalization, 30 represents the harvesting
		// season
		days = days % 30;
		double prob = days * 3.3;
		if (prob > farmer.luck) {
			InfoTableService infoS = new InfoTableServiceImpl();
			double demage = days/100 * ServiceInjector.randomGeneratorService.random(1.0, 5.0) * farmer.productQuantity;
			farmer.productQuantity -= demage;
			infoS.createT1(farmer, String.format("Во меѓувреме, крадец ви украде %s kg јаболки.",demage),"");
		}
	}

	@Override
	public Boolean hasProtectingNet(Farmer farmer) {
		ItemInstance item = ItemInstance.find("byType.name", "protecting-net")
				.first();
		if (item != null) {
			return true;
		}
		return false;

	}

}
