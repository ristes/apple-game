package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Farmer;
import models.HarvestingPeriod;
import models.PlantType;
import models.PlantationSeedling;
import models.SeedlingType;
import models.Yield;
import models.YieldPortion;
import models.YieldPortionSold;
import service.HarvestService;
import service.ServiceInjector;
import service.impl.HarvestServiceImpl;
import dto.HarvestingInfo;
import dto.StatusDto;

public class HarvestingController extends GameController {

	public static void harvest(int goodcollected, int goodtotal,
			int badcollected, int badtotal, long plantationseedling)
			throws Exception {

		Farmer farmer = checkFarmer();
		PlantationSeedling ps = PlantationSeedling.findById(plantationseedling);
		HarvestService hService = new HarvestServiceImpl();
		double goodper = goodcollected / (double) goodtotal;
		double badper = badcollected / (double) badtotal;
		farmer = hService.makeHarvesting(farmer, ps, goodper, badper);
		ServiceInjector.contextService.evaluateState(farmer);
		JsonController.statusJson(farmer);
	}

	public static void harvestingPeriod() {

		Farmer farmer = checkFarmer();

		List<PlantationSeedling> seedlings = PlantationSeedling.find(
				"plantation", farmer.field.plantation).fetch();

		List<HarvestingInfo> info = new ArrayList<HarvestingInfo>();
		for (PlantationSeedling ps : seedlings) {
			int recolteYear = ServiceInjector.dateService
					.recolteYear(farmer.gameDate.date);
			Yield yieldDone = Yield.find(
					"byYearAndFarmerAndPlantationSeedling", recolteYear,
					farmer, ps).first();
			if (yieldDone != null) {
				continue;
			}
			HarvestingPeriod period = ps.seedling.type.period;
			Calendar gameDate = Calendar.getInstance();
			gameDate.setTime(farmer.gameDate.date);

			Calendar start = Calendar.getInstance();
			Calendar end = Calendar.getInstance();
			start.setTime(period.startFrom);
			start.set(Calendar.YEAR, gameDate.get(Calendar.YEAR));
			end.setTime(period.endTo);
			end.set(Calendar.YEAR, gameDate.get(Calendar.YEAR));

			HarvestingInfo hi = new HarvestingInfo();
			long time = gameDate.getTimeInMillis() - start.getTimeInMillis();
			long days = time / 86400000;

			if (days > 9) {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/9.png";
			} else if (days < 0) {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/0.png";
			} else {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/" + days
						+ ".png";
			}

			hi.plantationSeedlignId = ps.id;
			hi.type = ps.seedling.type;
			hi.setIodineStarch(ps.seedling.type.minStraif+((ps.seedling.type.maxStraif - ps.seedling.type.minStraif)*(gameDate.getTimeInMillis() - start.getTimeInMillis())/(end.getTimeInMillis()-start.getTimeInMillis())));
//			hi.strength = 6.7;
//			hi.rfValue = 11.3;
			info.add(hi);
		}
		JsonController.toJson(info);
	}

	public static void getYield() {
		Farmer farmer = checkFarmer();
		int recolteYear = ServiceInjector.dateService
				.recolteYear(farmer.gameDate.date);
		List<Yield> yieldDone = Yield.find("year=?1 And farmer=?2",
				recolteYear, farmer).fetch();
		for (Yield y : yieldDone) {
			y.storedQuantity = 0;
			for (YieldPortion yp : y.yieldPortions) {
				y.storedQuantity += yp.quantity;
			}
			for (YieldPortionSold yp : y.yieldPortionsSold) {
				y.storedQuantity += yp.quantity;
			}
		}

		JsonController.toJson(yieldDone, "farmer", "plantation",
				"percentOfPlantedArea", "price", "period", "seedlingType");
	}
}
