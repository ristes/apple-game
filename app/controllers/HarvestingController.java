package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import dto.StatusDto;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;
import models.Farmer;
import models.Yield;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;
import service.HarvestService;
import service.impl.HarvestServiceImpl;

public class HarvestingController extends GameController {

	public static void harvest() throws Exception {

		Farmer farmer = checkFarmer();
		HarvestService hService = new HarvestServiceImpl();
		farmer = hService.makeHarvesting(farmer);

		StatusDto status = new StatusDto(true, "Успешна берба",
				String.valueOf(farmer.apples_in_stock), farmer);
		JsonController.toJson(status, FARMER_EXCLUDES);
	}

}
