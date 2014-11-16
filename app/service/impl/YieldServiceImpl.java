package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import service.DateService;
import service.FieldService;
import service.YieldService;
import dto.C;

public class YieldServiceImpl implements YieldService {

	public Double calculateYield(Farmer farmer) {
		DateService dateService = new DateServiceImpl();
		FieldService farmerService = new FieldServiceImpl();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int year = c.get(Calendar.YEAR);
		Integer ord_year = dateService.evaluateYearLevel(dateService
				.recolteYear(farmer.gameDate.date));
		List<PlantationSeedling> seedlings = PlantationSeedling.find(
				"byPlantation", farmer.field.plantation).fetch();
		Double terrainTypeYield = farmer.field.terrain.yieldCoef;
		double sum = 0.0;
		double applesPerA = 0.0;
		for (PlantationSeedling seedling : seedlings) {
			Long seedlingType = seedling.seedling.seedlingType.id;
			HashMap<Integer, ArrayList<Double>> hash = YmlServiceImpl
					.load_hash_key_int(C.COEF_SEEDLIGTYPE_YIELD);
			ArrayList<Double> coefs_yield = hash.get(seedlingType.intValue());
			Double seedling_coef = coefs_yield.get(ord_year - 1);
			applesPerA = farmer.field.plantation.base.maxApplesPerHa;
			applesPerA =  applesPerA * farmer.field.area * 1000
					* (farmer.field.plantation.fieldPercentage / 100.0);
			applesPerA = terrainTypeYield * applesPerA * (seedling_coef / 100.0);
			sum+=applesPerA;
		}
		if (farmerService.hasBees(farmer)) {
			sum+= sum*20/100;
		}
		return applesPerA;
	}

}
