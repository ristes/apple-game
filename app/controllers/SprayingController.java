package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import play.mvc.Controller;
import service.ContextService;
import service.FarmerService;
import service.ServiceInjector;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;

public class SprayingController extends Controller {

	public static void spray(Long itemid) throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		Item item = Item.findById(itemid);
		ItemInstance instance = ItemInstance.findById(itemid);
		if (instance.ownedBy.id != farmer.id) {
			renderJSON("");
		}
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
		executed.itemInstance = instance;
		executed.save();
		ServiceInjector.ecoPointsService.substract(farmer,1);	
		farmer.save();
		JsonController.statusJson(farmer);
	}

}
