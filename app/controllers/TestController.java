package controllers;

import models.Farmer;
import service.ServiceInjector;

public class TestController extends JsonController{
	
	public static void test() {
		Farmer farmer = AuthController.getFarmer();
		toJson(ServiceInjector.learnStateEventService.avail(farmer));
	}

}
