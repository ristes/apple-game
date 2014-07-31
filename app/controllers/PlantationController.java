package controllers;

import dto.StatusDto;
import models.Farmer;
import play.mvc.Controller;
import service.FarmerService;
import service.PlantingService;
import service.impl.FarmerServiceImpl;
import service.impl.PlantingServiceImpl;

public class PlantationController extends Controller {

	public static void savePlanting(String array, Integer seedlings) {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		FarmerService farmerService = new FarmerServiceImpl();
		farmerService.setState(farmer, FarmerService.STATE_GROWING);
		PlantingService plantingService = new PlantingServiceImpl();
		farmer = plantingService.savePlantingParams(farmer, array, seedlings);
		renderJSON(new StatusDto(true));
	}

}
