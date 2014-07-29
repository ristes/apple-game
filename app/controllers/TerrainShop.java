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

public class TerrainShop extends Controller {
	
	public static final Long price_Ha = 20000l;

	public static void allTerrains() throws Exception {
		JsonController.toJson(Terrain.findAll(), "analysis");
	}

	public static void analyze(Long terrainId) throws Exception {
		Terrain t = Terrain.findById(terrainId);
		Farmer farmer = AuthController.getFarmer();
		farmer.balans -= TerrainAnalysis.ANALYSIS_PRICE;
		if (farmer.balans > 0) {
			farmer.save();
		}
		JsonController.toJson(t.analysis.features, "category");
	}

	public static void buyTerrain(Long terrainId, Double size,
			String currentState) throws Exception {

		Field field = new Field();
		field.area = size;
		field.terrain = Terrain.findById(terrainId);

		Farmer farmer = AuthController.getFarmer();
		Double totalCost = size * price_Ha + field.terrain.analysis.unitPrice * size;
		if (farmer.balans < totalCost) {
			throw new NotEnoughMoneyException();
		}
		farmer.balans -= totalCost;
		farmer.currentState = currentState;
		farmer.field = field;

		if (farmer.balans > 0) {
			field.owner = farmer;
			field.save();
			farmer.save();
		}
		JsonController.farmerJson(farmer);
	}

	public static void allBases() throws Exception {
		JsonController.toJson(Base.findAll());
	}

	public static void buyBase(Long itemid, String currentState)
			throws Exception {
		Farmer farmer = AuthController.getFarmer();
		Field field = Field.find("owner.id", farmer.id).first();

		Plantation plantation = Plantation.buildInstance();
		plantation.base = Base.findById(itemid);

		field.plantation = plantation;

		farmer.currentState = currentState;
		farmer.balans -= plantation.base.price;

		if (farmer.balans > 0) {
			//farmer.productQuantity = (int) Math.round(YieldController.calculateYield());
			plantation.save();
			field.save();
			//farmer.evaluateState();
			farmer.save();
		}
		JsonController.farmerJson(farmer);
	}

	/**
	 * calculate the maximum yield that the farmer could receive
	 * 
	 * @param farmer
	 * @return kg of apples for that season
	 */


	public static void allSeedlings() throws Exception {
		JsonController.toJson(Seedling.findAll(), "seedlingType", "type");
	}

	public static void buySeedling(Long seedlingTypeId, Long plantTypeId,
			String currentState) throws Exception {
		Farmer farmer = AuthController.getFarmer();

		Plantation plantation = Plantation.find("field.owner.id", farmer.id)
				.first();

		plantation.seadlings = Seedling
				.find("type.id=:t and seedlingType.id=:st")
				.setParameter("t", plantTypeId)
				.setParameter("st", seedlingTypeId).first();

		farmer.currentState = currentState;
		farmer.balans -= plantation.seadlings.price * plantation.field.area;

		if (farmer.balans > 0) {
			plantation.save();
			farmer.save();
		}
		JsonController.farmerJson(farmer);
	}

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

}
