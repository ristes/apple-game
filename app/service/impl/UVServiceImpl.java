package service.impl;

import play.i18n.Messages;
import models.Disease;
import models.Farmer;
import service.ServiceInjector;
import service.UVService;

public class UVServiceImpl implements UVService{

	@Override
	public void impact(Farmer farmer) {
		if (farmer.gameDate.uvProb>0.55 && !ServiceInjector.fieldService.hasUVProtectingNet(farmer)) {
			double randV = ServiceInjector.randomGeneratorService.random(5.0, 20.0);
			Double demage =	ServiceInjector.farmerService.subtractProductQuantity(farmer, randV, true, Messages.getMessage("en", "uv_demage", ""));
			if (demage.equals(0.0)) {
				ServiceInjector.logFarmerDataService.logOccurredDisease(farmer, (Disease)Disease.find("byName", "UVDemage").first(), demage.intValue());
			}
		}
	}

}
