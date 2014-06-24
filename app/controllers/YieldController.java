package controllers;

import play.mvc.Controller;
import models.Farmer;

public class YieldController extends Controller{

	public static Double calculateYield() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		double applesPerA = farmer.field.plantation.base.maxApplesPerHa / 10.0;
		return applesPerA * farmer.field.area * 1000;

	}
	
}
