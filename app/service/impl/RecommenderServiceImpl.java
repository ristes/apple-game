package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.Field;
import models.OperationBestTimeInterval;
import models.Plantation;
import service.RecommenderService;
import service.ServiceInjector;
import dto.FertilizationItem;

public class RecommenderServiceImpl implements RecommenderService {

	@Override
	public List<FertilizationItem> fertilize(Farmer farmer) {
		List<FertilizationItem> result = new ArrayList<FertilizationItem>();
		List<OperationBestTimeInterval> osbt = OperationBestTimeInterval.find(
				"endTo>=?1 and startFrom <=?2 and terrainAnalyse=?3",
				ServiceInjector.dateService
						.convertDateTo70(farmer.gameDate.date),
				ServiceInjector.dateService
						.convertDateTo70(farmer.gameDate.date),
						farmer.field.terrain.analysis).fetch();
		
		for (OperationBestTimeInterval ins : osbt) {
			if (ins.fertilizationBestTime != null) {
				result.add(new FertilizationItem(ins));
			}
		}
		return result;
	}

}
