package service.impl;

import models.Plantation;
import models.TerrainAnalysis;
import service.PlantationService;
import service.RandomGeneratorService;

public class PlantationServiceImpl implements PlantationService{

	
	public Plantation buildInstance() {
		RandomGeneratorService rgS = new RandomGeneratorServiceImpl();
		Long rAnalyse = rgS.random(1l, 5l).longValue();
		TerrainAnalysis ta = TerrainAnalysis.findById(rAnalyse);
		Plantation p = new Plantation();
		p.treePositions = "[]";
		p.analyse = ta;
		p.save();

		return p;
	}
}
