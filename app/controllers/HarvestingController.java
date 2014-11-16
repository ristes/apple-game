package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.SeedlingType;
import service.HarvestService;
import service.impl.HarvestServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.HarvestingInfo;
import dto.StatusDto;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public class HarvestingController extends GameController {

	public static void harvest(int goodcollected, int goodtotal,
			int badcollected, int badtotal) throws Exception {

		Farmer farmer = checkFarmer();
		HarvestService hService = new HarvestServiceImpl();
		double goodper = goodcollected / (double) goodtotal;
		double badper = badcollected / (double) badtotal;
		farmer = hService.makeHarvesting(farmer, goodper, badper);

		StatusDto status = new StatusDto(true, "Успешна берба",
				String.valueOf(farmer.apples_in_stock), farmer);
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
