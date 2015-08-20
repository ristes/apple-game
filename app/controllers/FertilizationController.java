package controllers;

import dao.DaoInjector;
import models.ExecutedOperation;
import models.Farmer;
import models.ItemInstance;
import service.FertilizeService;
import service.ServiceInjector;
import service.impl.FertilizeServiceImpl;

public class FertilizationController extends GameController {

	public static void fertilize(Double n, Double p, Double k, Double ca,
			Double b, Double mg) throws Exception {

		Farmer farmer = checkFarmer();
		ServiceInjector.fertilizeService.fertilize(farmer, n, p, k, ca, b, mg);
		JsonController.statusJson(farmer);
	}
	

	/**
	 * 
	 * @param itemid
	 *            , id in database for fertilization
	 * @param quantity
	 */
	public static void fertilize1(Long itemid, Double quantity) {
		ItemInstance instance = ItemInstance.findById(itemid);
		if (instance == null) {
			error("Not such item.");
		}

		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in.");
		}
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
		executed.itemInstance = instance;
		executed.save();
	}

}
