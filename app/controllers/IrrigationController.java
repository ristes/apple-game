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
import service.ServiceInjector;
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
		ServiceInjector.farmerService.addCumulative(farmer, deltaCul);
		ServiceInjector.contextService.evaluateState(farmer);
		JsonController.statusJson(farmer);
	}

	public static void dropsIrrigation(Integer time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {

		Farmer farmer = checkFarmer();
		double result = 0.0;
		result = ServiceInjector.irrigationService
				.dropsIrrigation(farmer, time);

		returnFarmer(farmer, result);
	}

	public static void groovesIrrigation(Integer time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		double result = ServiceInjector.irrigationService.groovesIrrigation(farmer,
				time);

		returnFarmer(farmer, result);
	}

	public static void tensiometerTime() {
		Farmer farmer = checkFarmer();
		int time = 0;
		try {
			time = ServiceInjector.irrigationService.tensiometerTimeForIrr(farmer);
		} catch (NotAllowedException e) {
			e.printStackTrace();
		}
		renderJSON(time);
	}

	public static void activeIrrigationItem() {
		Farmer farmer = checkFarmer();
		ItemBoughtDto irrigationType = ServiceInjector.irrigationService
				.getActiveIrrigationType(farmer);
		renderJSON(irrigationType);
	}

}
