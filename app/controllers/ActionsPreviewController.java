package controllers;

import java.util.List;

import models.Farmer;
import models.LogFarmerData;

public class ActionsPreviewController extends JsonController{
	
	public static void log(Integer year, Integer typeLog) {
		Farmer farmer = AuthController.getFarmer();
		List<LogFarmerData> logs = LogFarmerData.find("farmer=?1 and recolteYear=?2 and typeLog=?3 order by id desc",farmer,year, typeLog).fetch();
		toJson(logs, "farmer");
	}

}
