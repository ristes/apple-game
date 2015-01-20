package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dto.C;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.LogFarmerData;
import models.Operation;
import play.i18n.Messages;
import service.LandTreatmanService;
import service.ServiceInjector;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import exceptions.TypeOfPlowingNotRecognized;

public class LandTreatmanServiceImpl implements LandTreatmanService {

	public Farmer executeDigging(Farmer farmer, Long id)
			throws NotEnoughMoneyException {
		Item digItem = Item.findById(id);
		Integer price = (int) (farmer.field.area * digItem.price);

		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer,
				-price);
		farmer.digging_coef = 0.0;
		farmer.productQuantity += farmer.productQuantity * 0.05;
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer,
				(Operation) Operation.find("byName", "digging").first(),null);
		return farmer;
	}

	public int diggingLevel(Farmer farmer) {
		Double coef = farmer.digging_coef;
		if (coef > 2.0) {
			return 3;
		}
		return coef.intValue() + 1;
	}

	public Farmer executePlowing(Farmer farmer, Integer type)
			throws TooWaterOnFieldException, SoilTooDryException,
			NotEnoughMoneyException, TypeOfPlowingNotRecognized {

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
		evaluatePlowingEcoPoints(farmer);
		farmer.grass_growth = 0.0;
		farmer.save();
		// ServiceInjector.logFarmerDataService.logExecutedOperation(farmer,
		// (Operation)Operation.find("byName","deep_plowing").first());
		return farmer;
	}

	public void firstDeepPlowing(Farmer farmer, Integer deep) {
		farmer.grass_growth = 0.0;
		farmer.field.firstDeepPlow = deep;
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer,
				(Operation) Operation.find("byName", "deep_plowing").first(),null);
		farmer.field.save();

	}

	public Boolean isFirstDeepPlowed(Farmer farmer) {
		int deep = farmer.field.firstDeepPlow;
		if (Math.abs(deep - LandTreatmanService.DEEP_PLOW_OPTIMAL_VALUE) < 10) {
			return true;
		}
		return false;
	}

	public void evaluatePlowingEcoPoints(Farmer farmer) {
		if (!hasEcoTractor(farmer)) {
			ServiceInjector.ecoPointsService.substract(farmer, 1);
		}
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
			throws SoilTooDryException, TooWaterOnFieldException,
			NotEnoughMoneyException, TypeOfPlowingNotRecognized {
		if (deep == null) {
			throw new TypeOfPlowingNotRecognized();
		}
		String type = LandTreatmanService.SHALLOW_PLOWING;
		if (deep > LandTreatmanService.LIMIT_PLOWING_TYPE) {
			type = LandTreatmanService.DEEP_PLOWING;
		}
		int level = ServiceInjector.humidityService.humidityLevel(farmer);

		Item plowing = (Item) Item.find("byName", type).fetch().get(0);
		Double coefTypeTractor = hasEcoTractor(farmer) ? 0.7 : 1.0;
		Integer price = 0;
		switch (level) {
		case 0:
			price = (int) (coefTypeTractor * 1.5 * plowing.price * (int) farmer.field.area);
			break;
		case 1:
			price = (int) (coefTypeTractor * 1 * plowing.price * (int) farmer.field.area);
			break;
		case 2:
			price = (int) (coefTypeTractor * 1.3 * plowing.price * (int) farmer.field.area);
			break;
		case 3:
			price = (int) (coefTypeTractor * 1.5 * plowing.price * (int) farmer.field.area);
			break;
		case 4:
			throw new TooWaterOnFieldException(
					Messages.get("controller.plowing.fail.toowater"));
		}

		try {
			ServiceInjector.moneyTransactionService.commitMoneyTransaction(
					farmer, -price);
		} catch (NotEnoughMoneyException ex) {
			throw ex;
		}
		if (deep > LandTreatmanService.LIMIT_PLOWING_TYPE) {
			ServiceInjector.logFarmerDataService.logExecutedOperation(
					farmer,
					(Operation) Operation.find("byName",
							LandTreatmanService.DEEP_PLOWING).first(),(double)deep);
		} else {
			ServiceInjector.logFarmerDataService.logExecutedOperation(
					farmer,
					(Operation) Operation.find("byName",
							LandTreatmanService.SHALLOW_PLOWING).first(),(double)deep);
		}
		return farmer;
	}

	@Override
	public Boolean hasEcoTractor(Farmer farmer) {
		Item ecoTractor = Item.find("byName", "EkoTraktor").first();
		List<Item> items = ItemInstance.find("byOwnedByAndType", farmer,
				ecoTractor).fetch();
		if (items.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<LogFarmerData> shallowPlowingOperationsForPrevRecolte(
			Farmer farmer) {
		int recolte = ServiceInjector.dateService
				.recolteYear(farmer.gameDate.date);
		Operation operation = Operation.find("byName",LandTreatmanService.SHALLOW_PLOWING).first();
		List<LogFarmerData> shallowPlowing = LogFarmerData.find(
				"byFarmerAndRecolteYearAndOperation", farmer, recolte - 1,
				operation).fetch();
		return shallowPlowing;
	}

	@Override
	public List<LogFarmerData> deepPlowingOperationsForPrevRecolte(Farmer farmer) {
		int recolte = ServiceInjector.dateService
				.recolteYear(farmer.gameDate.date);
		Operation operation = Operation.find("byName",LandTreatmanService.DEEP_PLOWING).first();
		List<LogFarmerData> deepPlowings = LogFarmerData.find(
				"byFarmerAndRecolteYearAndOperation", farmer, recolte - 1,
				operation).fetch();
		return deepPlowings;
	}

	public Boolean hasDeepPlowed(Farmer farmer) {
		List<LogFarmerData> logs = deepPlowingOperationsForPrevRecolte(farmer);
		for (LogFarmerData log : logs) {
			int deep = log.information.intValue();
			if (Math.abs(deep - LandTreatmanService.DEEP_PLOW_OPTIMAL_VALUE) < 10) {
				Calendar c = Calendar.getInstance();
				c.setTime(log.logdate);
				int month = c.get(Calendar.MONTH);
				if (month==Calendar.NOVEMBER) {
					return true;
				}
			}
		}
		return false;
	}
	
	public Boolean hasShallowPlowed(Farmer farmer) {
		List<LogFarmerData> logs = shallowPlowingOperationsForPrevRecolte(farmer);
		for (LogFarmerData log : logs) {
			int deep = log.information.intValue();
			if (Math.abs(deep - LandTreatmanService.SHALLOW_PLOWING_OPTIMAL_VALUE) < 10) {
				Calendar c = Calendar.getInstance();
				c.setTime(log.logdate);
				int month = c.get(Calendar.MONTH);
				if (month==Calendar.OCTOBER || month==Calendar.NOVEMBER) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void evalDiggingCoefs(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		int level = ServiceInjector.humidityService.humidityLevel(farmer);
		if (level >= 3) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(3);
		} else if (level == 2) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(2);
		} else if (level == 1) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(1);
		} else if (level == 0) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(0);
		}
	}

}
