package controllers;

import java.io.IOException;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import play.mvc.Controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ExecutedOperationController extends Controller {

	public static void execute(Long item_id, Double quantity)
			throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			redirect("/Crafty/login");
		}
		ItemInstance instance = ItemInstance.findById(item_id);
		if (instance == null) {
			response.status = 401;
			renderJSON("");
		}
		if (instance.ownedBy != farmer) {
			response.status = 401;
			renderJSON("");
		}
		Item item = instance.type;
		if (item == null) {
			response.status = 401;
			renderJSON("");
		}
		Long storeId = item.store.id;

		if (storeId == 2) {
			FertilizationController.fertilize1(instance.id, quantity);
		} else if (storeId == 3) {
			SprayingController.spray(instance.id);
		}

	}
}
