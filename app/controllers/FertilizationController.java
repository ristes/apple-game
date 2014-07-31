package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.FertilizerOperationDto;
import exceptions.NotEnoughMoneyException;
import models.ExecutedOperation;
import models.Farmer;
import models.FertilizationOperation;
import models.Item;
import models.ItemInstance;
import models.Operation;
import models.OperationBestTimeInterval;
import models.Store;
import play.db.jpa.JPA;
import play.mvc.Controller;
import service.DateService;
import service.FarmerService;
import service.FertilizeService;
import service.impl.DateServiceImpl;
import service.impl.FarmerServiceImpl;
import service.impl.FertilizeServiceImpl;

public class FertilizationController extends Controller {

	public static void fertilize(Double n, Double p, Double k, Double ca,
			Double b, Double mg) throws JsonGenerationException,
			JsonMappingException, IOException {

		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in.");
		}
		
		try {
			FertilizeService ferDao = new FertilizeServiceImpl();
			farmer = ferDao.fertilize(farmer, n, p, k, ca, b, mg);
		} catch (NotEnoughMoneyException e) {
			e.printStackTrace();
		}

		JsonController.farmerJson(farmer);
	}
	
	

	
	
	/**
	 * 
	 * @param itemid
	 *            , id in database for fertilization
	 * @param quantity
	 */
	public static void fertilize1(Long itemid, Double quantity) {
		ItemInstance instance = ItemInstance.findById(itemid);
		if (instance == null) {
			error("Not such item.");
		}

		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in.");
		}
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
		executed.itemInstance = instance;
		executed.save();
	}

	

	

	

	

}
