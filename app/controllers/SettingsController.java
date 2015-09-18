package controllers;

import dto.StatusDto;
import models.Farmer;
import play.mvc.Controller;
import service.ServiceInjector;

public class SettingsController extends Controller{
	
	public static void sounds(Boolean status) {
		Farmer farmer = AuthController.getFarmer();
		farmer.soundsEnabled = status;
		farmer.save();
		renderJSON(new StatusDto<>(true));
	}

}
