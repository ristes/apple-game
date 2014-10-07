package controllers;

import java.io.IOException;

import models.Farmer;
import service.HarvestService;
import service.impl.HarvestServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
		farmer = hService.makeHarvesting(farmer, goodper, badper);

		StatusDto status = new StatusDto(true, "Успешна берба",
				String.valueOf(farmer.apples_in_stock), farmer);
		JsonController.toJson(status, FARMER_EXCLUDES);
	}

}
