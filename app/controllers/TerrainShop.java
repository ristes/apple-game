package controllers;

import java.util.List;

import models.Base;
import models.PlantType;
import models.Seedling;
import models.SeedlingType;
import models.Terrain;
import play.mvc.Controller;

public class TerrainShop extends Controller {

	public static void allTerrains() throws Exception {
		JsonController.toJson(Terrain.findAll(), "analysis");
	}

	public static void buyTerrain(Long terrainId, Double size) {
		System.out.println(terrainId);

	}

	public static void analyze(Long terrainId) throws Exception {
		Terrain t = Terrain.findById(terrainId);
		JsonController.toJson(t.analysis.features, "category");
	}

	public static void allBases() throws Exception {
		JsonController.toJson(Base.findAll());
	}

	public static void buyBase(Long baseId) {

	}

	public static void allSeedlings() throws Exception {
		JsonController.toJson(Seedling.findAll(), "seedlingType", "type");
	}

	public static void buySeedling(Long seedlingId) {

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
