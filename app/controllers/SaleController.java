package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.StatusDto;
import exceptions.NotEnoughApplesException;
import exceptions.PriceNotValidException;
import models.Farmer;
import play.Play;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;
import service.AppleSaleTransactionService;
import service.impl.TransactionServiceImpl;
import service.impl.YmlServiceImpl;

public class SaleController extends Controller {


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


}
