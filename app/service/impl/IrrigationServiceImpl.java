package service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import controllers.AuthController;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Store;
import service.FieldService;
import service.IrrigationService;
import service.MoneyTransactionService;
import dao.ItemsDao;
import dao.impl.ItemsDaoImpl;
import dto.C;
import dto.ItemBoughtDto;
import dto.StoreItemDto;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public class IrrigationServiceImpl implements IrrigationService {

	public static final String GROOVES = "grooves";
	public static final String DROPS = "drops";
	public static final String TENSIOMETERS = "tensiometers";

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
				.get(C.ENUM_GROOVES).doubleValue()
				* area_size * time;
		MoneyTransactionService moneyTransServ = new TransactionServiceImpl();
		moneyTransServ.commitMoneyTransaction(farmer, -price);

		return result;
	}

	public int tensiometerTimeForIrr(Farmer farmer) throws NotAllowedException {
		FieldService fieldService = new FieldServiceImpl();
		if (fieldService.hasTensiometerSystem(farmer)) {

			double delta = farmer.deltaCumulative;
			HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
					.load_hash(C.COEF_HUMIDITY_YML);
			int time = 0;
			time = (int) (delta / coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES)
					.get(C.ENUM_DROPS));
			if (time > 0) {
				time = 0;
			}
			return Math.abs(time);
		} else {
			throw new NotAllowedException();
		}
	}

	@Override
	public ItemBoughtDto getActiveIrrigationType(Farmer farmer) {

		ItemsDao dao = new ItemsDaoImpl();
		List<ItemInstance> res = dao.getItemsByStoreNameOrdered(farmer.id,
				"irrigation");
		if (res == null || res.isEmpty()) {
			ItemInstance instance = new ItemInstance();
			instance.ownedBy = farmer;
			instance.type = dao.findItemByName(GROOVES);
			instance.save();
			return new ItemBoughtDto(instance);
		} else {
			return new ItemBoughtDto(res.get(0));
		}
	}

}
