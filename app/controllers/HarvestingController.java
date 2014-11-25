package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.PlantationSeedling;
import models.SeedlingType;
import service.HarvestService;
import service.impl.HarvestServiceImpl;
import dto.HarvestingInfo;
import dto.StatusDto;

public class HarvestingController extends GameController {

	public static void harvest(int goodcollected, int goodtotal,
			int badcollected, int badtotal, long plantationseedling) throws Exception {

		Farmer farmer = checkFarmer();
		PlantationSeedling ps = PlantationSeedling.findById(plantationseedling);
		HarvestService hService = new HarvestServiceImpl();
		double goodper = goodcollected / (double) goodtotal;
		double badper = badcollected / (double) badtotal;
		farmer = hService.makeHarvesting(farmer, ps, goodper, badper);
		StatusDto status = new StatusDto(true, "Uspesha berba","", farmer);
		JsonController.toJson(status, FARMER_EXCLUDES);
	}

	public static void harvestingPeriod() {
		// TODO: return real values
		List<HarvestingInfo> info = new ArrayList<HarvestingInfo>();

		HarvestingInfo hi = new HarvestingInfo();
		hi.iodineStarchUrl = "/public/images/game/harvest-test/07.png";
		hi.type = SeedlingType.findById(3);
		hi.iodineStarch = 4.8;
		hi.strength = 6.7;
		hi.rfValue = 11.3;
		info.add(hi);

		JsonController.toJson(info);

	}

}
