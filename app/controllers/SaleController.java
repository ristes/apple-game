package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.StatusDto;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;
import models.Farmer;
import models.Fridge;
import models.PlantType;
import service.ServiceInjector;

public class SaleController extends GameController {

	public static void sale(Integer fridge_type_id, Long plant_id,  int quantity) throws NotEnoughMoneyException, NotEnoughApplesException, PriceNotValidException, JsonGenerationException, JsonMappingException, IOException{
		Farmer farmer = checkFarmer();
		PlantType plantType = PlantType.findById(plant_id);
		Fridge fridge = Fridge.find("byFarmerAndType",farmer,fridge_type_id).first();
		ServiceInjector.sellService.sell(farmer, fridge, plantType, quantity);
		StatusDto statusDto = new StatusDto(true);
		statusDto.farmer = farmer;
		statusDto.additionalInfo = String.valueOf(quantity);
		JsonController.statusJson(statusDto);
	}

	/*
	public static void sale(Integer quantity) throws JsonGenerationException,
			JsonMappingException, IOException {
		Boolean status = false;
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login");
		}
		double prevBalanse = farmer.getBalance();
		double afterBalanse = farmer.getBalance();
		
		try {
			AppleSaleTransactionService appleSaleService = new TransactionServiceImpl();
			appleSaleService.commitAppleSaleTransaction(farmer, quantity);
			
		} catch (NotEnoughApplesException e) {
			StatusDto statusDto = new StatusDto(status, e.getMessage(),null, farmer);
			JsonController
					.toJson(statusDto, "gameDate", "field", "weatherType");
		} catch (PriceNotValidException e) {
			StatusDto statusDto = new StatusDto(status, e.getMessage(),null, farmer);
			JsonController
					.toJson(statusDto, "gameDate", "field", "weatherType");
		}
		afterBalanse = farmer.getBalance();
		status = true;
		StatusDto statusDto = new StatusDto(status, Messages.get("controller.sale.success",String.valueOf(quantity),afterBalanse-prevBalanse),String.valueOf(afterBalanse-prevBalanse), farmer);
		JsonController.toJson(statusDto, "gameDate", "field", "weatherType","plantation");

	}
	*/

}
