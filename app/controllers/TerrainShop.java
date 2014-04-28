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
import play.mvc.Controller;

public class TerrainShop extends Controller {

	public static void allTerrains() throws Exception {
		JsonController.toJson(Terrain.findAll(), "analysis");
	}

	public static void buyTerrain(Long terrainId, Double size) throws Exception {

		Field field = new Field();
		field.terrain = Terrain.findById(terrainId);
		field.save();

		Farmer farmer = AuthController.getFarmer();
		farmer.currentState = "baseShop";
		farmer.field = field;
		farmer.save();

		JsonController.toJson(farmer);
	}

	public static void analyze(Long terrainId) throws Exception {
		Terrain t = Terrain.findById(terrainId);
		JsonController.toJson(t.analysis.features, "category");
	}

	public static void allBases() throws Exception {
		JsonController.toJson(Base.findAll());
	}

	public static void buyBase(Long baseId) throws Exception {
		Farmer farmer = AuthController.getFarmer();
		Field field = Field.find("owner.id", farmer.id).first();

		Plantation plantation = new Plantation();
		plantation.base = Base.findById(baseId);
		plantation.save();

		field.plantation = plantation;
		field.save();

		farmer.currentState = "seedlingShop";
		farmer.save();
		JsonController.toJson(farmer);
	}

	public static void allSeedlings() throws Exception {
		JsonController.toJson(Seedling.findAll(), "seedlingType", "type");
	}

	public static void buySeedling(Long seedlingId) throws Exception {
		Farmer farmer = AuthController.getFarmer();

		Plantation plantation = Plantation.find("field.owner.id", farmer.id)
				.first();
		plantation.seadlings = Seedling.findById(seedlingId);
		plantation.save();

		farmer.currentState = "shop";
		farmer.save();
		JsonController.toJson(farmer);
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
