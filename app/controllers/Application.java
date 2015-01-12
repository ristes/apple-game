package controllers;

import java.io.IOException;
import java.util.List;
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
		String tip = ServiceInjector.tipService.randomTip(ServiceInjector.tipService.tipgenerator(farmer));
		JsonController.statusJson(farmer, tip);
	}

	public static void nextweek() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
		} else {
			ServiceInjector.farmerService.gotoNextWeek(farmer);
		}
		String tip = ServiceInjector.tipService.randomTip(ServiceInjector.tipService.tipgenerator(farmer));
		JsonController.statusJson(farmer, tip);
	}

	public static void nextmonth() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
		} else {
			farmer = ServiceInjector.farmerService.gotoNextMonth(farmer);
		}
		String tip = ServiceInjector.tipService.randomTip(ServiceInjector.tipService.tipgenerator(farmer));
		JsonController.statusJson(farmer, tip);
	}

	public static void restartGame() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		farmer = ServiceInjector.farmerService.restartGame(farmer);
		redirect("/iso");
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

	public static void rankings() throws Exception{
		List<Farmer> farmers = Farmer.find("is_active=?1",true).fetch();
		renderJSON(JsonController.toJsonString(farmers));
	}

	public static void badges() {
		Farmer farmer = getFarmer();
		ServiceInjector.badgesService.fertilizer(farmer);
		renderJSON(new StatusDto(true));
	}


}