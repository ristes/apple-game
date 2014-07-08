package controllers;

import dto.StatusDto;
import models.Farmer;
import play.mvc.Controller;

public class PlantationController extends Controller {

	public static void savePlanting(String array, Integer seedlings) {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		farmer.currentState = "growing";
		farmer.save();
		int optimum = 2380;
		switch (farmer.field.plantation.base.id.intValue()) {
		case 1:
			optimum = 1435;
			break;
		case 2:
			optimum = 1000;
			break;
		case 3:
		default:
			optimum = 1000;
			break;
		}
		farmer.field.plantation.fieldPercentage = (int) (seedlings * 35 * 100.0 / optimum);
		farmer.field.plantation.treePositions = array;
		farmer.field.plantation.save();
		renderJSON(new StatusDto(true));
	}

}
