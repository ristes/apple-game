package controllers;

import java.io.IOException;

import models.Farmer;
import play.cache.Cache;
import service.FarmerService;
import service.ServiceInjector;
import service.impl.FarmerServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;

public class Application extends GameController {

	public static void nextday() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
			renderJSON(new StatusDto(false));
		} else {
			ServiceInjector.farmerService.gotoNextDay(farmer);
		}
		farmer.save();
		JsonController.statusJson(farmer);
	}

	public static void nextweek() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
		} else {
			ServiceInjector.farmerService.gotoNextWeek(farmer);
		}
		JsonController.statusJson(farmer);
	}

	public static void nextmonth() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
		} else {
			farmer = ServiceInjector.farmerService.gotoNextMonth(farmer);
		}
		JsonController.statusJson(farmer);
	}

	public static void restartGame() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		farmer = ServiceInjector.farmerService.restartGame(farmer);
		redirect("/iso");
		JsonController.statusJson(farmer);
	}

	protected static Farmer getFarmer() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		if (fid != null) {
			Farmer farmer = Farmer.findById(fid);
			if (farmer.is_active) {
				return farmer;
			}
		}
		return null;
	}
}