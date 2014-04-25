package controllers;

import models.Terrain;
import play.mvc.Controller;

public class TerrainShop extends Controller {

	public static void allTerrains() throws Exception {
		JsonController.toJson(Terrain.findAll(), "analysis");
	}

	public static void buyTerrain(Long terrainId) {
		System.out.println(terrainId);

	}

	public static void analyze(Long terrainId) throws Exception {
		Terrain t = Terrain.findById(terrainId);
		JsonController.toJson(t.analysis.features);
	}

}
