package service.impl;

import models.Farmer;
import models.LogFarmerData;
import models.Operation;
import service.LogFarmerDataService;
import service.PrunningService;
import service.ServiceInjector;

public class PruneServiceImpl implements PrunningService{

	@Override
	public void prune(Farmer farmer, Integer good, Integer totalgood) {
		Operation operation = Operation.find("name=?1",PrunningService.OPERATION_NAME).first();
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer, operation, good/(double)totalgood);
	}

	@Override
	public LogFarmerData hasPruned(Farmer farmer) {
		return null;
	}

	@Override
	public double getDiminusher(Farmer farmer, LogFarmerData log) {
		Double diminusher = log.information;
		if (log==null) {
			return 0.4;
		}
		return 0.2;
		
	}

}
