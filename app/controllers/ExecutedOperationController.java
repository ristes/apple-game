package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import models.Farmer;
import models.Item;
import models.ItemInstance;

public class ExecutedOperationController extends Controller{
	public static void execute(Long item_id, Double quantity) {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login");
		}
		ItemInstance instance = ItemInstance.findById(item_id);
		if (instance==null) {
			response.status = 401;
			renderJSON("");
		}
		if (instance.ownedBy!=farmer) {
			response.status = 401;
			renderJSON("");
		}
		Item item = instance.type;
		if (item==null) {
			response.status = 401;
			renderJSON("");
		}
		Long storeId = item.store.id;
		
		if (storeId==2) {
			FertilizationController.fertilize(instance.id, quantity);
		} else if (storeId==3) {
			SprayingController.spray(instance.id);
		}
		
	}
}
