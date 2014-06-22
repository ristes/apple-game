package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;

import models.Farmer;
import models.Item;
import play.mvc.Controller;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.C;
import exceptions.NotEnoughMoneyException;

public class IrrigationController extends Controller {

	private static Farmer checkFarmer() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		return farmer;
	}

	private static void returnFarmer(Farmer farmer, double deltaCul)
			throws JsonGenerationException, JsonMappingException, IOException {
		farmer.deltaCumulative += deltaCul;
		if (farmer.deltaCumulative > 50) {
			farmer.deltaCumulative = 50d;
		}
		farmer.cumulativeHumidity = 0.0;
		farmer.evaluateState();
		farmer.save();


		JsonController.toJson(farmer, "field", "gameDate", "weatherType","plantation");
	}

	public static void dropsIrrigation(String time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {

		Farmer farmer = checkFarmer();
		double result = 0.0;

		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();
		if (farmer.field.hasDropSystem(farmer)) {
			int timeInt = Integer.parseInt(time);
			int coefSoil = farmer.coef_soil_type;
			double parH = coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
					C.ENUM_DROPS);
			result = timeInt * coefSoil * parH;
			Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES).get(
					C.ENUM_DROPS)
					* farmer.field.area * timeInt;
			if (price <= farmer.balans) {
				farmer.balans -= price;
			} else {
				throw new NotEnoughMoneyException(
						"Not enough money to irrigate.");
			}
		}

		returnFarmer(farmer, result);
	}

	public static void groovesIrrigation(String time)
			throws NotEnoughMoneyException, JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		double result = 0.0;

		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();

		result = Integer.parseInt(time)
				* farmer.coef_soil_type
				* coefs.get(C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(
						C.ENUM_GROOVES);

		Double area_size = farmer.field.area;
		Double price = coefs.get(C.KEY_PRICE_IRRIGATION_VALUES)
				.get(C.ENUM_DROPS).doubleValue()
				* area_size * Integer.parseInt(time);
		if (price <= farmer.balans) {
			farmer.balans -= price;
		} else {
			throw new NotEnoughMoneyException("Not enough money to irrigate.");
		}

		returnFarmer(farmer, result);
	}

	public static void tensiometerTime(int irrigationType) {
		Farmer farmer = checkFarmer();
		if (farmer.field.hasTensiometerSystem(farmer)) {

			double delta = farmer.deltaCumulative;
			HashMap<String, ArrayList<Double>> coefs = HumidityController
					.load_hash();
			int time = 0;
			if (C.ENUM_DROPS == irrigationType) {
				time = (int) (delta / coefs.get(
						C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_DROPS));
			} else if (C.ENUM_GROOVES == irrigationType) {
				time = (int) (delta / coefs.get(
						C.KEY_ONE_HOUR_IRRIGATION_VALUES).get(C.ENUM_GROOVES));
			}
			if (time > 0) {
				time = 0;
			} 
			renderJSON(Math.abs(time));
		}
	}

}
