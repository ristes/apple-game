package controllers;

import java.io.IOException;

import models.Farmer;
import play.mvc.Controller;
import service.ContextService;
import service.LandTreatmanService;
import service.impl.ContextServiceImpl;
import service.impl.LandTreatmanServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;

public class LandTreatmanController extends GameController {

	public static void digging() throws JsonGenerationException,
			JsonMappingException, IOException, NotEnoughMoneyException {
		Farmer farmer = checkFarmer();
		LandTreatmanService landTreatmanService = new LandTreatmanServiceImpl();
		farmer = landTreatmanService.executeDigging(farmer);
		ContextService ctxService = new ContextServiceImpl();
		ctxService.evaluateState(farmer);
		StatusDto status = new StatusDto(true, null, null, farmer);
		JsonController.toJson(status, "field", "gameDate", "weatherType");
	}

	public static void plowing() throws Exception {
		Farmer farmer = checkFarmer();
		LandTreatmanService landService = new LandTreatmanServiceImpl();
		farmer = landService.executePlowing(farmer);
		ContextService ctxService = new ContextServiceImpl();
		ctxService.evaluateState(farmer);
		StatusDto status = new StatusDto(true, null, null, farmer);
		JsonController.toJson(status, "field", "gameDate", "weatherType");

	}

}
