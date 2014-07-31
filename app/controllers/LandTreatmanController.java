package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;












import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.StatusDto;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import models.Farmer;
import models.Item;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;
import service.ContextService;
import service.HumidityService;
import service.LandTreatmanService;
import service.impl.ContextServiceImpl;
import service.impl.HumidityServiceImpl;
import service.impl.LandTreatmanServiceImpl;

public class LandTreatmanController extends Controller {
	
	public static void digging() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login?locale=mk");
		}
		try {
			LandTreatmanService landTreatmanService = new LandTreatmanServiceImpl();
			farmer = landTreatmanService.executeDigging(farmer);
		} catch (NotEnoughMoneyException ex) {
			StatusDto status = new StatusDto(false, ex.getMessage(), String.valueOf(ex.getPrice()), farmer);
			JsonController.farmerJson(farmer);;
		}
		ContextService ctxService = new ContextServiceImpl();
		ctxService.evaluateState(farmer);
		StatusDto status = new StatusDto(true,null,null,farmer);
		JsonController.toJson(status, "field", "gameDate","weatherType");
	}

	
	public static void plowing() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		try {
			LandTreatmanService landService = new LandTreatmanServiceImpl();
			farmer = landService.executePlowing(farmer);
		} catch (NotEnoughMoneyException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.getBalance()),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		} catch (SoilTooDryException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.deltaCumulative),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		} catch (TooWaterOnFieldException ex) {
			StatusDto status = new StatusDto(false,ex.getMessage(),String.valueOf(farmer.deltaCumulative),farmer);
			JsonController.toJson(status, "field", "gameDate","weatherType");
		}
		ContextService ctxService = new ContextServiceImpl();
		ctxService.evaluateState(farmer);
		StatusDto status = new StatusDto(true,null,null,farmer);
		JsonController.farmerJson(farmer);
		
	}

	

}
