package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.StatusDto;
import exceptions.NotEnoughMoneyException;
import models.Farmer;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;

public class HarvestingController extends Controller{
	
	public static void harvest() throws NotEnoughMoneyException, JsonGenerationException, JsonMappingException, IOException{
		
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login");
		}
		double expense = farmer.field.area * 3000;
		if (expense > farmer.balans) {
			throw new NotEnoughMoneyException();
		}
		int q = farmer.productQuantity;
		farmer.apples_in_stock += q;
		farmer.productQuantity = 0;
		farmer.save();
		StatusDto status = new StatusDto(true, "Успешна берба", String.valueOf(farmer.productQuantity), farmer);
		JsonController.toJson(status, "gameDate", "field", "weatherType","plantation");
	}

	
	
}
