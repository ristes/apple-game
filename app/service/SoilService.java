package service;

import java.util.List;

import models.Farmer;
import models.TerrainFeature;

public interface SoilService {
	
	public List<TerrainFeature> features(Farmer farmer);

}
