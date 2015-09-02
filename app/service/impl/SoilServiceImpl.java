package service.impl;

import java.util.ArrayList;
import java.util.List;

import dto.OperationBestTimeIntervalDto;
import models.Farmer;
import models.OperationBestTimeInterval;
import models.TerrainFeature;
import service.SoilService;

public class SoilServiceImpl implements SoilService {

	@Override
	public List<OperationBestTimeIntervalDto> features(Farmer farmer) {
		List<OperationBestTimeIntervalDto> result = new ArrayList<OperationBestTimeIntervalDto>();
		List<OperationBestTimeInterval> res = OperationBestTimeInterval.find("terrainAnalyse.id=?1",
				farmer.field.plantation.analyse.id).fetch();
		for (OperationBestTimeInterval i:res) {
			result.add(new OperationBestTimeIntervalDto(i));
		}
		return result;

	}

}
