package controllers;

import com.google.gson.Gson;

import dto.StatusDto;
import models.BugReport;
import models.Farmer;

public class BugReportController extends JsonController{

	public static void save( String body) {
		BugReport bugReport = new Gson().fromJson(body, BugReport.class);
		Farmer farmer = AuthController.getFarmer();
		bugReport.setFarmer(farmer);
		bugReport.save();
		renderJSON(new StatusDto<Boolean>(true));
	}
}
