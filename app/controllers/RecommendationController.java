package controllers;

import java.util.List;

import models.Farmer;
import service.ServiceInjector;
import dto.FertilizationItem;

public class RecommendationController extends JsonController{
	
	public static void ferilize() {
		Farmer farmer = AuthController.getFarmer();
		List<FertilizationItem> result = ServiceInjector.recommenderService.fertilize(farmer);
		toJson(result);
	}
	
	

}
