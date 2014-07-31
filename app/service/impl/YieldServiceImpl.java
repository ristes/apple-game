package service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import models.Farmer;

import org.yaml.snakeyaml.Yaml;

import play.Play;
import play.cache.Cache;
import controllers.AuthController;
import controllers.WeatherController;
import dto.C;
import service.DateService;
import service.YieldService;

public class YieldServiceImpl implements YieldService{

	
	
	public Double calculateYield(Farmer farmer) {
		DateService dateService = new DateServiceImpl();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int year = c.get(Calendar.YEAR);
		Integer ord_year = dateService.evaluateYearLevel(farmer.gameDate.date);
		Long seedlingType = farmer.field.plantation.seadlings.seedlingType.id;
		HashMap<Integer, ArrayList<Double>> hash = YmlServiceImpl.load_hash_key_int(C.COEF_SEEDLIGTYPE_YIELD);
		ArrayList<Double> coefs_yield = hash.get(seedlingType.intValue());
		Double seedling_coef = coefs_yield.get(ord_year);
		double applesPerA = farmer.field.plantation.base.maxApplesPerHa;
		applesPerA = applesPerA * farmer.field.area * 1000
				* (farmer.field.plantation.fieldPercentage / 100.0);
		applesPerA = applesPerA * (seedling_coef / 100.0);
		return applesPerA;

	}

}
