package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import models.Base;
import models.Farmer;
import models.PlantType;
import models.PlantationSeedling;
import models.Seedling;
import models.SeedlingType;
import service.FarmerService;
import service.PlantingService;
import service.ServiceInjector;
import service.SoilService;
import service.StoreService;
import service.impl.FarmerServiceImpl;
import service.impl.PlantingServiceImpl;
import service.impl.SoilServiceImpl;
import service.impl.StoreServiceImpl;

import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;

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
		for (Base base:bases) {
			base.description = String.format("You can plant maximum %d plants.",Integer.parseInt(String.valueOf((int)(base.maxTreePerHa*farmer.field.area))));
		}
		JsonController.toJson(bases);
	}

	
	public static void maxSeedlingsAllowed() {
		Farmer farmer = checkFarmer();
		StatusDto<Integer> result = new StatusDto<Integer>(true);
		result.t = (int)(farmer.field.area * farmer.field.plantation.base.maxTreePerHa);
		renderJSON(result);
	}
	public static void availableSeedlings() {
		Farmer farmer = checkFarmer();
		List<PlantationSeedling> res = PlantationSeedling.find(
				"plantation.field.owner.id", farmer.id).fetch();
		int num = 0;
		for (PlantationSeedling ps : res) {
			num += ps.quantity;
		}
		StatusDto<Integer> result = new StatusDto<Integer>(true);
		result.t = num;
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

	public static void buySeedlings(String nextState, Long s0, Long st0, Integer q0, Long s1, Long st1, Integer q1, Long s2, Long st2, Integer q2) throws Exception {
		Farmer farmer = checkFarmer();
		Integer totalPlants = (q0 != null ? q0 : 0) + (q1 != null ? q1 : 0)
				+ (q2 != null ? q2 : 0);
		
		List<PlantationSeedling> seedlings = new ArrayList<PlantationSeedling>();
		if (q0 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			Seedling seedling = Seedling.find("seedlingType.id=? and type.id=?", st0,s0).first();
			ps.seedling = seedling;
			ps.quantity = q0;
			ps.percentOfPlantedArea = q0 * 100 / totalPlants;
			seedlings.add(ps);
		}
		if (q1 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			Seedling seedling = Seedling.find("seedlingType.id=? and type.id=?", st1,s1).first();
			ps.seedling = seedling;
			ps.quantity = q1;
			ps.percentOfPlantedArea = q1 * 100 / totalPlants;
			seedlings.add(ps);
		}
		if (q2 != null) {
			PlantationSeedling ps = new PlantationSeedling();
			Seedling seedling = Seedling.find("seedlingType.id=? and type.id=?", st2,s2).first();
			ps.seedling = seedling;
			ps.quantity = q2;
			ps.percentOfPlantedArea = q2 * 100 / totalPlants;
			seedlings.add(ps);
		}
		ServiceInjector.storeService.buySeedling(farmer, seedlings, nextState);
		ServiceInjector.fridgeService.setFridges(farmer, seedlings);
		
		JsonController.statusJson(farmer);
		
	}
//	public static void buySeedlings(String nextState, Seedling s0, Integer q0,
//			Seedling s1, Integer q1, Seedling s2, Integer q2) throws Exception {
//		Farmer farmer = checkFarmer();
//		Integer totalPlants = (q0 != null ? q0 : 0) + (q1 != null ? q1 : 0)
//				+ (q2 != null ? q2 : 0);
//		// PlantingService plantingS = new PlantingServiceImpl();
//		// farmer = plantingS.savePlantingParams(farmer, totalPlants);
//		// Integer percentPlanted = farmer.field.plantation.fieldPercentage;
//		List<PlantationSeedling> seedlings = new ArrayList<PlantationSeedling>();
//		if (q0 != null) {
//			PlantationSeedling ps = new PlantationSeedling();
//			ps.seedling = s0;
//			ps.quantity = q0;
//			ps.percentOfPlantedArea = q0 * 100 / totalPlants;
//			seedlings.add(ps);
//		}
//		if (q1 != null) {
//			PlantationSeedling ps = new PlantationSeedling();
//			ps.seedling = s1;
//			ps.quantity = q1;
//			ps.percentOfPlantedArea = q1 * 100 / totalPlants;
//			seedlings.add(ps);
//		}
//		if (q2 != null) {
//			PlantationSeedling ps = new PlantationSeedling();
//			ps.seedling = s2;
//			ps.quantity = q2;
//			ps.percentOfPlantedArea = q2 * 100 / totalPlants;
//			seedlings.add(ps);
//		}
//		ServiceInjector.storeService.buySeedling(farmer, seedlings, nextState);
//		ServiceInjector.fridgeService.setFridges(farmer, seedlings);
//		
//		JsonController.statusJson(farmer);
//	}

	public static void buyBase(String nextState, Base base) throws Exception {
		Farmer farmer = checkFarmer();
		StoreService service = new StoreServiceImpl();
		service.buyBase(farmer, base.id, nextState);
		JsonController.statusJson(farmer);
	}

	public static void soilanalyse() throws Exception {
		SoilService soilService = new SoilServiceImpl();
		JsonController.toJson(soilService.features(checkFarmer()),
				"terrainAnalyse", "operation", "operationBestTimeInterval",
				"type", "attachments", "storeName", "attachedTo",
				"fertilizationOperations", "store", "imageurl", "metadata",
				"image", "perHa", "id", "pollutionCoefficient", "price");
	}
}
