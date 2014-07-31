package service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import models.Farmer;
import dto.C;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import service.FieldService;
import service.IrrigationService;
import service.MoneyTransactionService;

public class IrrigationServiceImpl implements IrrigationService {

	public double dropsIrrigation(Farmer farmer, Integer time)
			throws NotEnoughMoneyException {
		double result = 0.0;

		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		FieldService fieldService = new FieldServiceImpl();
		if (fieldService.hasDropSystem(farmer)) {
			int timeInt = time;
			int coefSoil = farmer.coef_soil_type;
			double parH = coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
					C.ENUM_DROPS);
			result = timeInt * coefSoil * parH;
			Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES).get(
					C.ENUM_DROPS)
					* farmer.field.area * timeInt;

			MoneyTransactionService moneyTransServ = new TransactionServiceImpl();
			moneyTransServ.commitMoneyTransaction(farmer, -price);
		}
		return result;
	}

	@Override
	public double groovesIrrigation(Farmer farmer, Integer time)
			throws NotEnoughMoneyException {
		double result = 0.0;

		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);

		result = time
				* farmer.coef_soil_type
				* coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
						C.ENUM_GROOVES);

		Double area_size = farmer.field.area;
		Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES)
				.get(C.ENUM_DROPS).doubleValue()
				* area_size * time;
		MoneyTransactionService moneyTransServ = new TransactionServiceImpl();
		moneyTransServ.commitMoneyTransaction(farmer, -price);
		
		return result;
	}

	public int tensiometerTimeForIrr(Farmer farmer,int irrigationType) throws NotAllowedException{
		FieldService fieldService = new FieldServiceImpl();
		if (fieldService.hasTensiometerSystem(farmer)) {

			double delta = farmer.deltaCumulative;
			HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
					.load_hash(C.COEF_HUMIDITY_YML);
			int time = 0;
			if (C.ENUM_DROPS == irrigationType) {
				time = (int) (delta / coefs.get(
						C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_DROPS));
			} else if (C.ENUM_GROOVES == irrigationType) {
				time = (int) (delta / coefs.get(
						C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_GROOVES));
			}
			if (time > 0) {
				time = 0;
			}
			return Math.abs(time);
		}
		else {
			throw new NotAllowedException();
		}
	}
}
