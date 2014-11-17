package service.impl;

import java.util.List;

import models.Farmer;
import models.OperationBestTimeInterval;
import models.TerrainFeature;
import service.SoilService;

public class SoilServiceImpl implements SoilService {

	@Override
	public List<OperationBestTimeInterval> features(Farmer farmer) {

		return OperationBestTimeInterval.find("terrainAnalyse.id=?1",
				farmer.field.plantation.analyse.id).fetch();

	}

}
