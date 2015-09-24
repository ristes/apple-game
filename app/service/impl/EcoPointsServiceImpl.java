package service.impl;

import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import play.i18n.Messages;
import models.Farmer;
import models.InfoTable;
import service.EcoPointsService;
import service.MoneyTransactionService;
import service.ServiceInjector;

public class EcoPointsServiceImpl implements EcoPointsService {

	@Override
	public void add(Farmer farmer, Integer value) {
		if (value < 0) {
			throw new NumberFormatException();
		}
		farmer.setEco_points(farmer.getEco_points() + value);

	}

	@Override
	public void substract(Farmer farmer, Integer value) {
		if (value < 0) {
			throw new NumberFormatException();
		}
		int currentEco = (int) (farmer.getEco_points() - value);

		farmer.setEco_points(currentEco);
		ServiceInjector.gameEndService.evaluate(farmer);

	}

	@Override
	public Integer divide(Farmer farmer, Double value) {
		if (value <= 0) {
			throw new NumberFormatException();
		}
		Integer substract = (int)(farmer.getEco_points()
				/ value);
		farmer.setEco_points(farmer.getEco_points() - substract);
		return substract;

	}

	@Override
	public void restart(Farmer farmer) {
		Double ecos = farmer.getEco_points();
		try {
			if (farmer.isPersistent()) {
				getMoneyForEcoPoints(farmer);
			}
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		farmer.setEco_points(ECO_START_VALUE);
	}

	private void getMoneyForEcoPoints(Farmer farmer)
			throws NotEnoughMoneyException {
		ServiceInjector.logFarmerDataService.logFinalEcoPoints(farmer,
				farmer.getEco_points());
		Double money = farmer.getEco_points() * COEF_ECO_TO_MONEY;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer,
				money);
		String info = Messages.get("earn_from_ecos", money);
		farmer.setEco_points(0.0);
		farmer.save();
		ServiceInjector.infoTableService.createT1(farmer, info,
				"/public/images/elems/coinbucket.jpg");
	}

}
