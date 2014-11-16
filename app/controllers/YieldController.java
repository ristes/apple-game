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
import service.YieldService;
import service.impl.YieldServiceImpl;
import models.Farmer;

public class YieldController extends Controller {

	
	public static void calculate() {
		Farmer farmer = AuthController.getFarmer();
		YieldService yield = new YieldServiceImpl();
		renderJSON(yield.calculateYield(farmer));
	}
	
}
