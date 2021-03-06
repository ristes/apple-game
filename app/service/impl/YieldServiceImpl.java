package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import models.Farmer;
import models.Item;
import models.LogFarmerData;
import models.PlantType;
import models.PlantationSeedling;
import models.Yield;
import service.LogFarmerDataService;
import service.ServiceInjector;
import service.YieldService;
import dto.C;

public class YieldServiceImpl implements YieldService {

	public Double calculateYield(Farmer farmer) {
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
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
			applesPerA =  applesPerA * farmer.field.area * 1000
					* (farmer.field.plantation.fieldPercentage / 100.0);
			applesPerA = terrainTypeYield * applesPerA * (seedling_coef / 100.0);
			sum+=applesPerA;
		}
		LogFarmerData pruneLog = null;
		if ((pruneLog=ServiceInjector.prunningService.hasPruned(farmer))!=null) {
			sum -= sum*ServiceInjector.prunningService.getDiminusher(farmer, pruneLog);
		}
		if (ServiceInjector.itemInstanceService.has(farmer,"bees")) {
			sum+= sum*10/100;
		}
		if (!ServiceInjector.landTreatmanService.isFirstDeepPlowed(farmer)) {
			sum-=sum*10/100;
		}
		if (!ServiceInjector.landTreatmanService.hasDeepPlowed(farmer)) {
			sum-=sum*5/100;
		}
		if (!ServiceInjector.landTreatmanService.hasShallowPlowed(farmer)) {
			sum-=sum*5/100;
		}
		return sum;
	}

	@Override
	public List<Yield> getPreviousYearYield(Farmer farmer) {
		int year = ServiceInjector.dateService.fridgerecolteyear(farmer.gameDate.date);
		return Yield.find("byFarmerAndYear", farmer, year).fetch();
	}

	@Override
	public Double getMaxYieldByRecolte(Farmer farmer, Integer recolte) {
		LogFarmerData data = LogFarmerData.find("typelog=?1 AND farmer=?2 AND recolteYear=?3", LogFarmerDataService.MAX_YIELD, farmer,recolte).first();
		if (data==null) {
			return 0.0;
		}
		return data.information;
	}

	@Override
	public Boolean areApplesByTypeAndYearHarvested(Farmer farmer,
			PlantType plantType, Integer year) {
		if (Yield
				.find("byFarmerAndYearAndPlantationSeedling.seedling.type.id",
						farmer, year, plantType.id).fetch().size() == 0) {
			return false;
			
		}
		return true;
	}

}
