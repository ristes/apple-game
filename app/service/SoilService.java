package service;

import java.util.List;

import models.Farmer;
import models.OperationBestTimeInterval;
import models.TerrainFeature;

public interface SoilService {
	
	public List<OperationBestTimeInterval> features(Farmer farmer);

}
