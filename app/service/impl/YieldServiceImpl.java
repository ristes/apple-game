package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import models.Yield;
import service.ServiceInjector;
import service.YieldService;
import dto.C;

public class YieldServiceImpl implements YieldService {

	public Double calculateYield(Farmer farmer) {
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int year = c.get(Calendar.YEAR);
		Integer ord_year = ServiceInjector.dateService.evaluateYearLevel(ServiceInjector.dateService
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
//			seedling.plantation.fieldPercentage
			applesPerA =  applesPerA * farmer.field.area * 1000
					* (farmer.field.plantation.fieldPercentage / 100.0);
			applesPerA = terrainTypeYield * applesPerA * (seedling_coef / 100.0);
			sum+=applesPerA;
		}
		if (ServiceInjector.farmerService.hasBees(farmer)) {
			sum+= sum*10/100;
		}
		return applesPerA;
	}

	@Override
	public List<Yield> getPreviousYearYield(Farmer farmer) {
		int year = ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date);
		return Yield.find("byFarmerAndYear", farmer, year-1).fetch();
	}

}
