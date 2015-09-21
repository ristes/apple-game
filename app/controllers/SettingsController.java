package controllers;

import dto.StatusDto;
import models.Farmer;
import play.mvc.Controller;
import service.ServiceInjector;

public class SettingsController extends GameController{
	
	public static void sounds(Boolean status) {
		Farmer farmer = AuthController.getFarmer();
		farmer.soundsEnabled = status;
		farmer.save();
		StatusDto<Farmer> result = new StatusDto<Farmer>(true);
		result.t = farmer;
		JsonController.toJson(result, "field");
	}

}
