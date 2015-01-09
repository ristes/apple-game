package controllers;

import java.io.IOException;

import models.Farmer;
import models.Item;
import service.ContextService;
import service.LandTreatmanService;
import service.ServiceInjector;
import service.impl.ContextServiceImpl;
import service.impl.LandTreatmanServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;
import exceptions.NotEnoughMoneyException;

public class LandTreatmanController extends GameController {

	public static void digging(Long id) throws JsonGenerationException,
			JsonMappingException, IOException, NotEnoughMoneyException {
		Farmer farmer = checkFarmer();
		farmer = ServiceInjector.landTreatmanService.executeDigging(farmer, id);

		ServiceInjector.contextService.evaluateState(farmer);
		StatusDto status = new StatusDto(true, null, null, farmer);
		JsonController.toJson(status, "field", "gameDate", "weatherType");
	}

	public static void plowing(Integer deep) throws Exception {
		Farmer farmer = checkFarmer();
		ServiceInjector.landTreatmanService.executePlowing(farmer, deep);
		ServiceInjector.contextService.evaluateState(farmer);
		StatusDto status = new StatusDto(true, null, null, farmer);
		JsonController.toJson(status, "field", "gameDate", "weatherType");

	}
	
	public static void firstdeep(Integer deep, String nextState) throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		ServiceInjector.landTreatmanService.firstDeepPlowing(farmer, deep);
		farmer.currentState = nextState;
		farmer.save();
		JsonController.statusJson(farmer);
	}

}
