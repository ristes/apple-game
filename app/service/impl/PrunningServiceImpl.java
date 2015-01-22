package service.impl;

import models.DeceaseProtectingOperation;
import models.ExecutedOperation;
import models.Farmer;
import models.LogFarmerData;
import models.Operation;
import service.LogFarmerDataService;
import service.PrunningService;
import service.ServiceInjector;

public class PrunningServiceImpl implements PrunningService{

	@Override
	public void prune(Farmer farmer, Double percent) {
		Operation operation = Operation.find("name=?1",PrunningService.OPERATION_NAME).first();
		ExecutedOperation prunning = new ExecutedOperation();
		prunning.field = farmer.field;
		prunning.startDate = farmer.gameDate.date;
		prunning.operation = operation;
		prunning.save();
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer, operation, percent);
	}

	@Override
	public LogFarmerData hasPruned(Farmer farmer) {
		return null;
	}

	@Override
	public double getDiminusher(Farmer farmer, LogFarmerData log) {
		int year_level = ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date);
		if (null==log) {
			if (year_level == 1) {
				return 0.4;
			} else {
				return 0.2;
			}
		}
		if (year_level==1) {
			return log.information*0.4;
		} 
		return log.information*0.1;
	}

}
