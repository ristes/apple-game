package service.impl;

import java.util.List;

import models.Farmer;
import models.TerrainFeature;
import service.SoilService;

public class SoilServiceImpl implements SoilService{

	@Override
	public List<TerrainFeature> features(Farmer farmer) {
		return farmer.field.plantation.analyse.features;
	}

}
