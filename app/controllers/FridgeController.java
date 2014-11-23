package controllers;

import java.util.List;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import service.ServiceInjector;
import dao.FridgeUsageDto;
import exceptions.InvalidYield;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public class FridgeController extends GameController{

	
	public static void farmerFridges() {
		Farmer farmer = checkFarmer();
		List<FridgeUsageDto> list = ServiceInjector.fridgeService.getFarmerFridges(farmer);
		renderJSON(list);
	}
	
	public static void buycapacity(Integer fridgetype, Integer capacity) throws NotEnoughMoneyException {
		Farmer farmer = checkFarmer();
		ServiceInjector.fridgeService.buyFridgeCapacity(farmer, capacity, fridgetype);
		List<FridgeUsageDto> list = ServiceInjector.fridgeService.getFarmerFridges(farmer);
		renderJSON(list);
	}
	
	public static void addtofridge(Integer type, long plant_type, int quantity) throws NotEnoughSpaceInFridge, InvalidYield{
		Farmer farmer = checkFarmer();
		PlantType plantType = PlantType.findById(plant_type);
		Fridge fridge = ServiceInjector.fridgeService.getFridge(farmer, type);
		ServiceInjector.fridgeService.addToFridge(farmer, fridge, plantType, quantity);
		farmerFridges();
	}
}
