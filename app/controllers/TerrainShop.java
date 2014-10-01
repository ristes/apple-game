package controllers;

import java.util.List;

import models.Base;
import models.Farmer;
import models.Field;
import models.PlantType;
import models.Plantation;
import models.Seedling;
import models.SeedlingType;
import models.Terrain;
import models.TerrainAnalysis;
import exceptions.NotEnoughMoneyException;
import play.mvc.Controller;
import service.MoneyTransactionService;
import service.StoreService;
import service.impl.StoreServiceImpl;
import service.impl.TransactionServiceImpl;

public class TerrainShop extends Controller {

	public static final Long price_Ha = 20000l;

	public static void allTerrains() throws Exception {
		JsonController.toJson(Terrain.findAll(), "analysis");
	}

	public static void analyze(Long terrainId) throws Exception {
		Terrain t = Terrain.findById(terrainId);
		Farmer farmer = AuthController.getFarmer();
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		moneyService.commitMoneyTransaction(farmer,
				-TerrainAnalysis.ANALYSIS_PRICE);
		JsonController.toJson(t.analysis.features, "category");
	}

	public static void buyTerrain(Long terrainId, Double size,
			String currentState) throws Exception {

		Field field = new Field();
		field.area = size;
		field.terrain = Terrain.findById(terrainId);

		Farmer farmer = AuthController.getFarmer();
		Double totalCost = size * price_Ha + field.terrain.analysis.unitPrice
				* size;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		moneyService.commitMoneyTransaction(farmer, -totalCost);
		farmer.currentState = currentState;
		farmer.field = field;

		field.owner = farmer;
		field.save();
		farmer.save();
		JsonController.farmerJson(farmer);
	}

	public static void allBases() throws Exception {
		JsonController.toJson(Base.findAll());
	}

	public static void buyBase(Long itemid, String currentState)
			throws Exception {
		Farmer farmer = AuthController.getFarmer();
		StoreService storeService = new StoreServiceImpl();
		farmer = storeService.buyBase(farmer, itemid, currentState);
		JsonController.farmerJson(farmer);
	}

	public static void allSeedlings() throws Exception {
		JsonController.toJson(Seedling.findAll(), "seedlingType", "type");
	}

	public static void buySeedling(Long seedlingTypeId, Long plantTypeId,
			String currentState) throws Exception {
		Farmer farmer = AuthController.getFarmer();

		StoreService storeService = new StoreServiceImpl();
		farmer = storeService.buySeedling(farmer, seedlingTypeId, plantTypeId, currentState);
		JsonController.farmerJson(farmer);
	}
	//TODO: Check this code productivity, otherwise delete it
/*
	public static void generateAllSeedlings() {
		List<SeedlingType> stList = SeedlingType.findAll();
		List<PlantType> ptList = PlantType.findAll();
		for (SeedlingType st : stList) {
			for (PlantType pt : ptList) {
				Seedling s = new Seedling();
				s.seedlingType = st;
				s.type = pt;
				s.price = 1;
				s.save();
			}
		}
	}
*/
}
