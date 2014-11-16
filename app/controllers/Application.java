package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.DiseaseOccurenceProb;
import dto.DiseaseStatusDto;
import dto.StatusDto;
import models.Day;
import models.Farmer;
import play.cache.Cache;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.With;
import service.ContextService;
import service.FarmerService;
import service.TipService;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;
import service.impl.TipServiceImpl;

public class Application extends GameController {

	public static void index() {
		
	}

	public static void nextday() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
			renderJSON(new StatusDto(false));
		} else {
			FarmerService farmerService = new FarmerServiceImpl();
			farmerService.gotoNextDay(farmer);
		}
		farmer.save();
		TipService tipService = new TipServiceImpl();
		String tip = tipService.randomTip(tipService.tipgenerator(farmer));

		StatusDto status = new StatusDto(true, "", "", farmer);
		status.tip = tip;
		// JsonController.farmerJson(farmer);
		JsonController.statusJson(status);
	}

	public static void nextweek() throws IOException {
		Farmer farmer = getFarmer();
		StatusDto statusDto = null;
		if (farmer == null) {
			statusDto = new StatusDto(false);
		} else {
			FarmerService farmerService = new FarmerServiceImpl();
			farmerService.gotoNextWeek(farmer);
			statusDto = new StatusDto(true, "", "", farmer);
		}

		TipService tipService = new TipServiceImpl();
		String tip = tipService.randomTip(tipService.tipgenerator(farmer));
		
		statusDto.tip = tip;
		// JsonController.farmerJson(farmer);
		JsonController.statusJson(statusDto);
	}

	public static void nextmonth() throws IOException {
		Farmer farmer = getFarmer();
		StatusDto statusDto = null;
		if (farmer == null) {
			statusDto = new StatusDto(false);
		} else {
			FarmerService farmerService = new FarmerServiceImpl();
			farmer = farmerService.gotoNextMonth(farmer);
			statusDto = new StatusDto(true, "", "", farmer);
		}
		TipService tipService = new TipServiceImpl();
		String tip = tipService.randomTip(tipService.tipgenerator(farmer));
		statusDto.tip = tip;
		JsonController.statusJson(statusDto);
	}

	public static void restartGame() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		FarmerService farmerService = new FarmerServiceImpl();
		farmer = farmerService.restartGame(farmer);
		redirect("/iso");
		StatusDto status = new StatusDto(true, "", "", farmer);
		// JsonController.farmerJson(farmer);
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
	/*
	 * public static void observeDiseasesForThisYear() { Farmer farmer =
	 * AuthController.getFarmer(); if (farmer == null) { renderJSON(""); }
	 * FarmerService farmerService = new FarmerServiceImpl();
	 * List<DiseaseStatusDto> result = new ArrayList<DiseaseStatusDto>();
	 * HashMap<String, Double> maxValues = new HashMap<String, Double>(); Double
	 * maxProb = 0.0; for (int i = 0; i < 365; i++) { Day gameDate =
	 * farmer.gameDate; farmer = farmerService.gotoNextDay(farmer);
	 * List<DiseaseOccurenceProb> dopList = DeseasesExpertSystem .getDP(farmer);
	 * for (DiseaseOccurenceProb dop : dopList) { if (dop.probability >
	 * (farmer.getLuck() * 100)) { DiseaseStatusDto status = new
	 * DiseaseStatusDto(); status.diseaseName = dop.name; status.status = true;
	 * status.dateOccurence = gameDate.date; status.diseaseProb =
	 * dop.probability; result.add(status);
	 * 
	 * } if (maxValues.containsKey(dop.name)) { if (maxValues.get(dop.name) <
	 * dop.probability) { maxValues.put(dop.name, dop.probability); } } else {
	 * maxValues.put(dop.name, dop.probability); } } } if (result.size() == 0) {
	 * renderJSON(maxValues); } renderJSON(result); }
	 */
}