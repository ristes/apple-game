package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.h2.table.Plan;

import models.Base;
import models.Farmer;
import models.Field;
import models.PlantType;
import models.Plantation;
import models.PlantationSeedling;
import models.Seedling;
import models.SeedlingType;
import service.FarmerService;
import service.PlantingService;
import service.SoilService;
import service.StoreService;
import service.impl.FarmerServiceImpl;
import service.impl.PlantingServiceImpl;
import service.impl.SoilServiceImpl;
import service.impl.StoreServiceImpl;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;
import exceptions.NotEnoughMoneyException;

public class PlantationController extends GameController {

	public static void seedlings() {
		List<Seedling> res = Seedling.all().fetch();
		JsonController.toJson(res, "yields");
	}

	public static void plantTypes() {
		List<PlantType> res = PlantType.all().fetch();
		JsonController.toJson(res);
	}

	public static void seedlingTypes() {
		List<SeedlingType> res = SeedlingType.all().fetch();
		JsonController.toJson(res, "yields");
	}

	public static void availableBases() {
		Farmer farmer = checkFarmer();
		List<PlantationSeedling> res = PlantationSeedling.find(
				"plantation.field.owner.id", farmer.id).fetch();
		int num = 0;
		for (PlantationSeedling ps : res) {
			num += ps.quantity;
		}
		List<Base> bases = Base.find("from models.Base where maxTreePerHa>?1",
				num).fetch();
		JsonController.toJson(bases);
	}

	public static void availableSeedlings() {
		Farmer farmer = checkFarmer();
		List<PlantationSeedling> res = PlantationSeedling.find(
				"plantation.field.owner.id", farmer.id).fetch();
		int num = 0;
		for (PlantationSeedling ps : res) {
			num += ps.quantity;
		}
		JsonController.toJson(num);
	}

	public static void savePlanting(String array, Integer seedlings)
			throws JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		FarmerService farmerService = new FarmerServiceImpl();
		farmerService.setState(farmer, FarmerService.STATE_GROWING);
		PlantingService plantingService = new PlantingServiceImpl();
		farmer = plantingService.savePlantingParams(farmer, array, seedlings);
		JsonController.statusJson(farmer);
	}

	public static void buySeedlings(String nextState, Seedling s0, Integer q0,
			Seedling s1, Integer q1, Seedling s2, Integer q2) throws Exception {
		Farmer farmer = checkFarmer();
		Integer totalPlants = (q0!=null?q0:0)+(q1!=null?q1:0)+(q2!=null?q2:0);
		PlantingService plantingS = new PlantingServiceImpl();
//		farmer = plantingS.savePlantingParams(farmer,  totalPlants);
//		Integer percentPlanted = farmer.field.plantation.fieldPercentage;
		StoreService service = new StoreServiceImpl();
		List<PlantationSeedling> seedlings = new ArrayList<PlantationSeedling>();
		if (q0 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			ps.seedling = s0;
			ps.quantity = q0;
			ps.percentOfPlantedArea = q0*100/totalPlants;
			seedlings.add(ps);
		}
		if (q1 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			ps.seedling = s1;
			ps.quantity = q1;
			ps.percentOfPlantedArea = q1*100/totalPlants;
			seedlings.add(ps);
		}
		if (q2 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			ps.seedling = s2;
			ps.quantity = q2;
			ps.percentOfPlantedArea = q2*100/totalPlants;
			seedlings.add(ps);
		}
		service.buySeedling(farmer, seedlings, nextState);
		JsonController.statusJson(farmer);
	}

	public static void buyBase(String nextState, Base base) throws Exception {
		Farmer farmer = checkFarmer();
		StoreService service = new StoreServiceImpl();
		service.buyBase(farmer, base.id, nextState);
		JsonController.statusJson(farmer);
	}

	public static void soilanalyse() throws Exception{
		SoilService soilService = new SoilServiceImpl();
		String res = JsonController.toJsonString(soilService.features(checkFarmer()), "field", "gameDate",
				"weatherType", "plantation","category","analysis","fertilizingBestTimeIntervals");
		renderJSON(res);
	}
}
