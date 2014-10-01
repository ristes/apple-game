package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;

import models.Farmer;
import models.Item;
import play.mvc.Controller;
import service.ContextService;
import service.FarmerService;
import service.FieldService;
import service.IrrigationService;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;
import service.impl.FieldServiceImpl;
import service.impl.IrrigationServiceImpl;
import service.impl.YmlServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.ItemBoughtDto;
import dto.StoreItemDto;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public class IrrigationController extends GameController {

	private static void returnFarmer(Farmer farmer, double deltaCul)
			throws JsonGenerationException, JsonMappingException, IOException {
		FarmerService farmerService = new FarmerServiceImpl();
		ContextService contextService = new ContextServiceImpl();
		farmerService.addCumulative(farmer, deltaCul);
		contextService.evaluateState(farmer);
		JsonController.farmerJson(farmer);
	}

	public static void dropsIrrigation(Integer time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {

		Farmer farmer = checkFarmer();
		double result = 0.0;

		try {
			IrrigationService irrService = new IrrigationServiceImpl();
			result = irrService.dropsIrrigation(farmer, time);
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}

		returnFarmer(farmer, result);
	}

	public static void groovesIrrigation(Integer time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		double result = 0.0;
		try {
			IrrigationService irrService = new IrrigationServiceImpl();
			result = irrService.groovesIrrigation(farmer, time);
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		returnFarmer(farmer, result);
	}

	public static void tensiometerTime() {
		Farmer farmer = checkFarmer();
		int time = 0;
		try {
			IrrigationService irrService = new IrrigationServiceImpl();
			time = irrService.tensiometerTimeForIrr(farmer);
		} catch (NotAllowedException e) {
			e.printStackTrace();
		}
		renderJSON(time);
	}

	public static void activeIrrigationItem() {
		Farmer farmer = checkFarmer();
		IrrigationService irrService = new IrrigationServiceImpl();
		ItemBoughtDto irrigationType = irrService
				.getActiveIrrigationType(farmer);
		renderJSON(irrigationType);
	}

}
