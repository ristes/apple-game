package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import models.Farmer;
import models.HarvestingPeriod;
import models.PlantType;
import models.PlantationSeedling;
import models.SeedlingType;
import service.HarvestService;
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
		StatusDto status = new StatusDto(true, "Uspesha berba", "", farmer);
		JsonController.toJson(status, FARMER_EXCLUDES);
	}

	public static void harvestingPeriod() {

		Farmer farmer = checkFarmer();

		List<PlantationSeedling> seedlings = PlantationSeedling.find(
				"plantation", farmer.field.plantation).fetch();

		List<HarvestingInfo> info = new ArrayList<HarvestingInfo>();
		for (PlantationSeedling ps : seedlings) {
			HarvestingPeriod period = ps.seedling.type.period;
			Calendar gameDate = Calendar.getInstance();
			gameDate.setTime(farmer.gameDate.date);

			Calendar start = Calendar.getInstance();
			start.setTime(period.startFrom);
			start.set(Calendar.YEAR, gameDate.get(Calendar.YEAR));

			HarvestingInfo hi = new HarvestingInfo();
			long time = start.getTimeInMillis() - gameDate.getTimeInMillis();
			long days = time / 864000;

			if (days > 9) {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/9.png";
			} else if (days < 0) {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/0.png";
			} else {
				hi.iodineStarchUrl = "/public/images/game/harvest-test/" + days
						+ ".png";
			}

			hi.type = ps.seedling.type;
			hi.iodineStarch = 4.8;
			hi.strength = 6.7;
			hi.rfValue = 11.3;
			info.add(hi);
		}
		JsonController.toJson(info);

	}
}
