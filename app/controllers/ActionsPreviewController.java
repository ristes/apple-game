package controllers;

import java.util.List;

import service.ServiceInjector;
import models.Farmer;
import models.LogFarmerData;

public class ActionsPreviewController extends JsonController{
	
	public static void log(Integer year, Integer typeLog) {
		Farmer farmer = AuthController.getFarmer();
		List<LogFarmerData> logs = ServiceInjector.logFarmerDataService.getFarmerExecutedOperationsForYear(farmer, year);
		toJson(logs, "farmer");
	}

}
