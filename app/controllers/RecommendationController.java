package controllers;

import java.util.List;

import models.Farmer;
import service.ServiceInjector;
import dto.FertilizationItem;
import exceptions.NotEnoughMoneyException;

public class RecommendationController extends JsonController{
	
	public static void ferilize() {
		Farmer farmer = AuthController.getFarmer();
		List<FertilizationItem> result = ServiceInjector.recommenderService.fertilize(farmer);
		toJson(result);
	}
	public static void ferilizeWitPayment() throws NotEnoughMoneyException{
		Farmer farmer = AuthController.getFarmer();
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -100);
		List<FertilizationItem> result = ServiceInjector.recommenderService.fertilize(farmer);
		toJson(result);
	}
	
	

}
