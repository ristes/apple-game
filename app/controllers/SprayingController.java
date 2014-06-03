package controllers;

import models.ExecutedOperation;
import models.Farmer;
import models.ItemInstance;
import play.mvc.Controller;

public class SprayingController extends Controller{
	
	public static void executeoperation(String itemid) {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			renderJSON("");
		}
		ItemInstance instance = ItemInstance.findById(Long.parseLong(itemid));
		if (instance.ownedBy.id!=farmer.id) {
			renderJSON("");
		}
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
	}

}
