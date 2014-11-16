package service.impl;

import java.util.List;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import play.i18n.Messages;
import service.FarmerService;
import service.HumidityService;
import service.LandTreatmanService;
import service.MoneyTransactionService;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;

public class LandTreatmanServiceImpl implements LandTreatmanService{

	
	public Farmer executeDigging(Farmer farmer) throws NotEnoughMoneyException{
		Item digItem = Item.find("byName", "DiggingItem").first();
		Integer price = (int)(farmer.field.area * digItem.price);

		MoneyTransactionService moneyTransServ = new TransactionServiceImpl();
		moneyTransServ.commitMoneyTransaction(farmer, -price);
		farmer.digging_coef = 0.0;
		farmer.productQuantity += farmer.productQuantity*0.05;
		return farmer;
	}
	
	public int diggingLevel(Farmer farmer) {
		Double coef = farmer.digging_coef;
		if (coef>2.0) {
			return 3;
		}
		return coef.intValue()+1;
	}

	public Farmer executePlowing(Farmer farmer) throws TooWaterOnFieldException, SoilTooDryException, NotEnoughMoneyException{
		
		try {
			farmer = determineThePlowingPrice(farmer);
		} catch (TooWaterOnFieldException ex) {
			throw ex;
		} catch (SoilTooDryException ex) {
			throw ex;
		} catch (NotEnoughMoneyException ex) {
			throw ex;
		}
		farmer = evaluatePlowingEcoPoints(farmer);
		farmer.grass_growth = 0.0;
		farmer.save();
		return farmer;
	}
	
	public Farmer evaluatePlowingEcoPoints(Farmer farmer) {
		FarmerService farmerS = new FarmerServiceImpl();
		if (!hasEcoTractor(farmer)) {
			farmerS.subtractEcoPoints(farmer, 1);
		}
		return farmer;
	}
	
	/**
	 * return 3 - high need of plowing return 2 - medium return 1 - not need
	 * 
	 * @param farmer
	 * @return
	 */
	public int plowingLevel(Farmer farmer) {
		if (farmer.grass_growth > 8) {
			return 4;
		} else if (farmer.grass_growth > 5) {
			return 3;
		} else if (farmer.grass_growth > 2) {
			return 2;
		}
		return 1;
	}
	
	public Farmer determineThePlowingPrice(Farmer farmer)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException {
		HumidityService hService = new HumidityServiceImpl();
		int level = hService.humidityLevel(farmer);
		Item plowing = (Item) Item.find("byName", "PlowingItem").fetch().get(0);
		Double coefTypeTractor =  hasEcoTractor(farmer)?0.7:1.0;
		Integer price = 0;
		switch (level) {
		case 0:
			throw new SoilTooDryException(Messages.get("controller.plowing.fail.toodry"));
		case 1:
			price =  (int) (coefTypeTractor * 1 * plowing.price * (int) farmer.field.area);
			break;
		case 2:
			price =  (int) (coefTypeTractor * 1.3 * plowing.price * (int) farmer.field.area);
			break;
		case 3:
			price =  (int) (coefTypeTractor * 1.5 * plowing.price * (int) farmer.field.area);
			break;
		case 4:
			throw new TooWaterOnFieldException(Messages.get("controller.plowing.fail.toowater"));
		}

		MoneyTransactionService moneyService = new TransactionServiceImpl();
		try {
			moneyService.commitMoneyTransaction(farmer, -price);
		} catch (NotEnoughMoneyException ex ) {
			throw ex;
		}
		return farmer;
	}

	@Override
	public Boolean hasEcoTractor(Farmer farmer) {
		Item ecoTractor = Item.find("byName", "EkoTraktor").first();
		List<Item> items = ItemInstance.find("byOwnedByAndType", farmer,ecoTractor).fetch();
		if (items.size()>0) {
			return true;
		}
		return false;
	}

}
