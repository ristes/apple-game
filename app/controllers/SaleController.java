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
import models.Farmer;
import play.Play;
import play.cache.Cache;
import play.i18n.Messages;
import play.mvc.Controller;

public class SaleController extends Controller {

	public static HashMap<String, ArrayList<Double>> load_hash() {
		String key = "COEFFICIENTS_HUM";
		HashMap<String, ArrayList<Double>> coefs = (HashMap<String, ArrayList<Double>>) Cache
				.get(key);
		if (coefs == null) {
			File securityFile = Play.getFile(C.COEF_SALES_YML);
			InputStream input = null;
			try {
				input = new FileInputStream(securityFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Yaml yaml = new Yaml();
			coefs = (HashMap<String, ArrayList<Double>>) yaml.load(input);
		}
		return coefs;
	}

	public static void sale(Integer quantity) throws JsonGenerationException,
			JsonMappingException, IOException {
		Boolean status = false;
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("/Crafty/login");
		}
		int prevBalanse = farmer.balans;
		int afterBalanse = farmer.balans;
		if (farmer == null) {
			redirect("/Crafty/login");
		}
		try {
			farmer = makeTransaction(farmer, quantity);
			afterBalanse = farmer.balans;
			status = true;
		} catch (NotEnoughApplesException e) {
			StatusDto statusDto = new StatusDto(status, e.getMessage(),null, farmer);
			JsonController
					.toJson(statusDto, "gameDate", "field", "weatherType");
		}
		StatusDto statusDto = new StatusDto(status, Messages.get("controller.sale.success",String.valueOf(quantity),afterBalanse-prevBalanse),String.valueOf(afterBalanse-prevBalanse), farmer);
		JsonController.toJson(statusDto, "gameDate", "field", "weatherType","plantation");

	}

	public static Farmer makeTransaction(Farmer farmer, Integer quantity)
			throws NotEnoughApplesException {
		HashMap<String, ArrayList<Double>> coefs = load_hash();
		if (farmer.apples_in_stock >= quantity) {
			Calendar c = Calendar.getInstance();
			Date gameDate = farmer.gameDate.date;
			c.setTime(gameDate);
			double cena = coefs.get(C.KEY_SALE_PRICES).get(
					c.get(Calendar.MONTH));
			farmer.apples_in_stock -= quantity;
			farmer.balans += quantity * cena;
		} else {
			throw new NotEnoughApplesException(Messages.get("controller.sale.fail"));
		}
		farmer.save();
		return farmer;
	}

}
