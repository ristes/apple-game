package controllers;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import service.ServiceInjector;
import dao.FridgeUsageDto;
import exceptions.InvalidYield;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public class FridgeController extends GameController {

	public static void farmerFridges() {
		Farmer farmer = checkFarmer();
		List<FridgeUsageDto> list = ServiceInjector.fridgeService
				.getFarmerFridges(farmer);
		renderJSON(list);
	}

	public static void buycapacity(Integer fridgetype, Integer capacity)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		ServiceInjector.fridgeService.buyFridgeCapacity(farmer, capacity,
				fridgetype);
		ServiceInjector.fridgeService.getFarmerFridges(farmer);
		JsonController.statusJson(farmer);
	}

	public static void addtofridge(Integer type, long plant_type, int quantity)
			throws NotEnoughSpaceInFridge, InvalidYield {
		Farmer farmer = checkFarmer();
		PlantType plantType = PlantType.findById(plant_type);
		Fridge fridge = ServiceInjector.fridgeService.getFridge(farmer, type);
		ServiceInjector.fridgeService.addToFridge(farmer, fridge, plantType,
				quantity);
		farmerFridges();
	}

	public static void removeFromFridge(Integer type, long plant_type,
			int quantity) throws NotEnoughSpaceInFridge, InvalidYield,
			NotEnoughApplesException {
		Farmer farmer = checkFarmer();
		PlantType plantType = PlantType.findById(plant_type);
		Fridge fridge = ServiceInjector.fridgeService.getFridge(farmer, type);
		ServiceInjector.fridgeService.removeFromFridge(farmer, fridge,
				plantType, quantity);
		farmerFridges();
	}
}
