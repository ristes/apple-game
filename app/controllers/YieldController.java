package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import dto.C;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import models.Farmer;

public class YieldController extends Controller {

	public final static int START_YEAR = 2022;

	public static Double calculateYield() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int year = c.get(Calendar.YEAR);
		Integer ord_year = WeatherController.evaluateYearLevel(farmer.gameDate.date);
		Long seedlingType = farmer.field.plantation.seadlings.seedlingType.id;
		HashMap<Integer, ArrayList<Double>> hash = load_hash();
		ArrayList<Double> coefs_yield = hash.get(seedlingType.intValue());
		Double seedling_coef = coefs_yield.get(ord_year);
		double applesPerA = farmer.field.plantation.base.maxApplesPerHa;
		applesPerA = applesPerA * farmer.field.area * 1000
				* (farmer.field.plantation.fieldPercentage / 100.0);
		applesPerA = applesPerA * (seedling_coef / 100.0);
		return applesPerA;

	}

	public static HashMap<Integer, ArrayList<Double>> load_hash() {
		String key = C.KEY_SEEDLNIG_TYPE_YIELD;
		HashMap<Integer, ArrayList<Double>> coefs = (HashMap<Integer, ArrayList<Double>>) Cache
				.get(key);
		if (coefs == null) {
			File securityFile = Play.getFile(C.COEF_SEEDLIGTYPE_YIELD);
			InputStream input = null;
			try {
				input = new FileInputStream(securityFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Yaml yaml = new Yaml();
			coefs = (HashMap<Integer, ArrayList<Double>>) yaml.load(input);
		}
		return coefs;
	}

}
