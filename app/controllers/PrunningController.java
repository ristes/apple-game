package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import service.ServiceInjector;
import models.Farmer;

public class PrunningController extends GameController{
	
	public static void prune(Double goodPercent) throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		ServiceInjector.prunningService.prune(farmer, goodPercent);
		JsonController.statusJson(farmer);
	}

}
