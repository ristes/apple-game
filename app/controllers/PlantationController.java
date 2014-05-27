package controllers;

import dto.StatusDto;
import models.Farmer;
import play.mvc.Controller;

public class PlantationController extends Controller{
	
	public static void savePlanting(String array) {
		Farmer  farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		farmer.field.plantation.treePositions = array;
		farmer.field.plantation.save();
		farmer.save();
		renderJSON(new StatusDto(true));
	}

}
