package service.impl;

import play.i18n.Messages;
import models.Farmer;
import service.ServiceInjector;
import service.UVService;

public class UVServiceImpl implements UVService{

	@Override
	public void impact(Farmer farmer) {
		if (farmer.gameDate.uvProb>0.55 && !ServiceInjector.fieldService.hasUVProtectingNet(farmer)) {
			double demage = ServiceInjector.randomGeneratorService.random(5.0, 20.0);
			ServiceInjector.farmerService.subtractProductQuantity(farmer, demage, true, Messages.getMessage("en", "uv_demage", ""));
		}
	}

}
