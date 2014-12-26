package service.impl;

import java.util.List;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Operation;
import play.i18n.Messages;
import service.LandTreatmanService;
import service.ServiceInjector;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import exceptions.TypeOfPlowingNotRecognized;

public class LandTreatmanServiceImpl implements LandTreatmanService{

	
	public Farmer executeDigging(Farmer farmer, Long id) throws NotEnoughMoneyException{
		Item digItem = Item.findById(id);
		Integer price = (int)(farmer.field.area * digItem.price);

		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -price);
		farmer.digging_coef = 0.0;
		farmer.productQuantity += farmer.productQuantity*0.05;
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer, (Operation)Operation.find("byName","digging").first());
		return farmer;
	}
	
	public int diggingLevel(Farmer farmer) {
		Double coef = farmer.digging_coef;
		if (coef>2.0) {
			return 3;
		}
		return coef.intValue()+1;
	}

	public Farmer executePlowing(Farmer farmer, Integer type) throws TooWaterOnFieldException, SoilTooDryException, NotEnoughMoneyException, TypeOfPlowingNotRecognized{
		
		try {
			farmer = determineThePlowingPrice(farmer, type);
		} catch (TooWaterOnFieldException ex) {
			throw ex;
		} catch (SoilTooDryException ex) {
			throw ex;
		} catch (NotEnoughMoneyException ex) {
			throw ex;
		} catch (TypeOfPlowingNotRecognized ex) {
			throw ex;
		}
		farmer = evaluatePlowingEcoPoints(farmer);
		farmer.grass_growth = 0.0;
		farmer.save();
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer, (Operation)Operation.find("byName","deep_plowing").first());
		return farmer;
	}
	
	public Farmer evaluatePlowingEcoPoints(Farmer farmer) {
		if (!hasEcoTractor(farmer)) {
			ServiceInjector.farmerService.subtractEcoPoints(farmer, 1);
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
	
	public Farmer determineThePlowingPrice(Farmer farmer, Integer deep)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException, TypeOfPlowingNotRecognized {
		if (deep==null) {
			throw new TypeOfPlowingNotRecognized();
		}
		String type = LandTreatmanService.SHALLOW_PLOWING;
		if (deep>45){
			type = LandTreatmanService.DEEP_PLOWING;
		}
		int level = ServiceInjector.humidityService.humidityLevel(farmer);
		
		Item plowing = (Item) Item.find("byName", type).fetch().get(0);
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

		
		
		try {
			ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -price);
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
