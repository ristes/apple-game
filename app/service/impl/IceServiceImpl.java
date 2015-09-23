package service.impl;

import java.util.Calendar;

import play.i18n.Messages;
import models.Farmer;
import service.IceService;
import service.ServiceInjector;

public class IceServiceImpl implements IceService {

	
	@Override
	public void impactLowTemp(Farmer farmer) {
		if (farmer.gameDate.tempHigh>0 && farmer.gameDate.tempLow<-5
				&& (((farmer.month_level - 1) == Calendar.MARCH) || ((farmer.month_level - 1) == Calendar.APRIL))) {
			if (!ServiceInjector.fieldService.hasArtificialRain(farmer)) {
				Double demage = ServiceInjector.randomGeneratorService.random(
						5.0, 10.0);
				Double demageQuantity = ServiceInjector.farmerService.subtractProductQuantity(farmer,
						demage, true,
						Messages.getMessage("en", "low_temp_demage", ""));
				if (ServiceInjector.insuranceService.hasInsuranceThisYear(farmer)) {
					ServiceInjector.insuranceService.refundByQuantityLost(farmer, demageQuantity.intValue());
				}
			}
		}
	}

}
