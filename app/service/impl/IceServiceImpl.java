package service.impl;

import java.util.Calendar;

import play.i18n.Messages;
import models.Farmer;
import service.IceService;
import service.ServiceInjector;

public class IceServiceImpl implements IceService {

	// @Override
	// public void impactIceProb(Farmer farmer) {
	// if (farmer.gameDate.iceProb>0.25 &&
	// ((farmer.month_level-1)==Calendar.MARCH)) {
	// double demage = ServiceInjector.randomGeneratorService.random(10.0,
	// 30.0);
	// ServiceInjector.farmerService.subtractProductQuantity(farmer, demage,
	// true, "");
	// }
	//
	// }
	@Override
	public void impactLowTemp(Farmer farmer) {
		if ((farmer.gameDate.tempHigh - farmer.gameDate.tempLow) / 2.0 < -1.0
				&& (((farmer.month_level - 1) == Calendar.MARCH) || ((farmer.month_level - 1) == Calendar.APRIL))) {
			if (!ServiceInjector.fieldService.hasArtificialRain(farmer)) {
				double demage = ServiceInjector.randomGeneratorService.random(
						10.0, 30.0);
				ServiceInjector.farmerService.subtractProductQuantity(farmer,
						demage, true,
						Messages.getMessage("en", "low_temp_demage", ""));
			}
		}
	}

}
