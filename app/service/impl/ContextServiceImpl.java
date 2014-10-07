package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import controllers.DeseasesExpertSystem;
import controllers.FertilizationController;
import controllers.HumidityController;
import controllers.LandTreatmanController;
import controllers.WeatherController;
import controllers.YieldController;
import dto.C;
import exceptions.NotSuchItemException;
import models.Day;
import models.Farmer;
import service.ContextService;
import service.DateService;
import service.DiseaseService;
import service.FertilizeService;
import service.FieldService;
import service.GrowingService;
import service.HumidityDropsService;
import service.HumidityGroovesService;
import service.HumidityService;
import service.LandTreatmanService;
import service.YieldService;

public class ContextServiceImpl implements ContextService {

	public Double generateLuck(Farmer farmer) {
		Random random = new Random();
		Double stand_dev = farmer.luck_dev;
		Double avg = farmer.luck_avg;
		farmer.luck = (random.nextGaussian() * stand_dev + avg);
		if (farmer.luck < (avg - stand_dev)) {
			farmer.luck = avg - stand_dev;
		}
		return farmer.luck;
	}

	public double calculateHumidityLooses(Farmer farmer) {
		double result = 0.0;
		FieldService fieldService = new FieldServiceImpl();
		if (!fieldService.hasDropSystem(farmer)) {
			HumidityGroovesService hGroovesService = new HumidityServiceImpl();
			farmer.productQuantity = hGroovesService
					.grooves_irrigation_delta_impact_quantity(farmer);
			farmer.eco_points = hGroovesService
					.grooves_irrigation_delta_impact_eco_point(farmer);
			result = hGroovesService.varianceBrazdi(farmer);
		} else {
			HumidityDropsService hDropsService = new HumidityServiceImpl();
			farmer.productQuantity = hDropsService
					.drops_irrigation_delta_impact_quantity(farmer);
			farmer.eco_points = hDropsService
					.drops_irrigation_delta_impact_eco_point(farmer);
			result = hDropsService.varianceDrops(farmer);
		}
		return result;
	}

	public Double rainCoefForMonth(Date date) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		double avg_rain = coefs.get(C.KEY_RAIN_COEFS)
				.get(c.get(Calendar.MONTH));
		return avg_rain;
	}

	public Double rainCoefForMonth(Integer month) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		double avg_rain = coefs.get(C.KEY_RAIN_COEFS).get(month);
		return avg_rain;
	}

	public void calculateCumulatives(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		Day today = farmer.gameDate;
		if (today.weatherType.id == C.WEATHER_TYPE_RAINY) {
			// double avg_rain = coefs.get(C.KEY_RAIN_COEFS).get(
			// c.get(Calendar.MONTH));
			farmer.deltaCumulative += rainCoefForMonth(c.get(Calendar.MONTH));
		}

		if (farmer.gameDate.dayOrder % 8 == 0) {
			double variance = calculateHumidityLooses(farmer);
			farmer.cumulativeHumidity += variance;
			farmer.deltaCumulative = variance;
			Double min_hum = coefs.get(C.KEY_MIN_HUMIDITY).get(0);
			if (farmer.deltaCumulative < min_hum) {
				farmer.deltaCumulative = min_hum;
			}
		}
		// do not allow humidity above the max
		double max_humidity = coefs.get(C.KEY_MAX_HUMIDITY).get(0);
		if (farmer.deltaCumulative > max_humidity) {
			farmer.deltaCumulative = max_humidity;
		}
	}

	public void calculateGrassGrowth(Farmer farmer) {
		farmer.grass_growth += 0.2;
	}

	public void calculateDiggingCoefficient(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		HumidityService hService = new HumidityServiceImpl();
		int level = hService.humidityLevel(farmer);
		if (level >= 3) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(3);
		} else if (level == 2) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(2);
		} else if (level == 1) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(1);
		} else if (level == 0) {
			farmer.digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(0);
		}
	}

	public void evaluateSoilImage(Farmer farmer) {
		String folder_path = "/public/images/game/soil_types/";
		String name_prefix = "";
		String separator = "";
		String extension = ".png";
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		if (c.get(Calendar.MONTH) == 11
				|| (c.get(Calendar.MONTH) < 2 && c.get(Calendar.DAY_OF_MONTH) <= 31)) {
			farmer.soil_url = "/public/images/game/soil_types/soil-snow.png";
			farmer.grass_growth = 0.0;
			farmer.digging_coef = 0.0;
			return;
		}
		HumidityService hService = new HumidityServiceImpl();
		LandTreatmanService landTreatmanS = new LandTreatmanServiceImpl();
		DateService dateService = new DateServiceImpl();
		int hum_level = hService.humidityLevelForSoil(hService
				.humidityLevel(farmer));
		int plowing_level = landTreatmanS.plowingLevel(farmer);
		int digging_level = landTreatmanS.diggingLevel(farmer);
		int season_level = seasionLevelSoilImage(dateService
				.season_level(farmer));
		String tile_name = folder_path + name_prefix + separator + hum_level
				+ separator + plowing_level + separator + digging_level
				+ separator + season_level + extension;

		farmer.soil_url = tile_name;
	}

	public void evaluatePlantImage(Farmer farmer) {
		GrowingService growService = new GrowingServiceImpl();
		farmer.plant_url = growService.evaluatePlantImage(farmer);
	}

	public void evaluateSeason(Farmer farmer) {
		DateService dateService = new DateServiceImpl();
		farmer.season_level = dateService.season_level(farmer);
	}

	public void evaluateDisease(Farmer farmer) {
		// check for diseases every 5 days triggered by the farmer luck
		// in winter the diseases does not occure
		if (farmer.season_level != C.SEASON_WINTER) {
			if (farmer.gameDate.dayOrder % 5 == 0) {
				DiseaseService diseaseService = new DiseaseServiceImpl();
				diseaseService.diseases(farmer);
			}
		}
	}

	public void evaluateRestartState(Farmer farmer) {
		DateService dateService = new DateServiceImpl();
		YieldService yieldService = new YieldServiceImpl();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (dateService.evaluateYearLevel(farmer.gameDate.date) >= 2) {
			if (month == 9 && day == 1) {
				farmer.productQuantity = (int) Math.round(yieldService
						.calculateYield(farmer));
				farmer.eco_points = 100;
			}
		}
	}

	public void evaluateFertilizingState(Farmer farmer) {
		DateService dateService = new DateServiceImpl();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month == Calendar.SEPTEMBER && day == 1
				&& dateService.evaluateYearLevel(farmer.gameDate.date) > 1) {
			FertilizeService fertService = new FertilizeServiceImpl();
			fertService.finalEvaluationFertilizer(farmer);
		}
	}

	public void calculateLuck(Farmer farmer) {
		if (farmer.gameDate.dayOrder % 5 == 0) {
			farmer.luck = generateLuck(farmer);
		}
	}

	public void evaluateState(Farmer farmer) {
		calculateCumulatives(farmer);
		calculateFertalizing(farmer);
		calculateLuck(farmer);
		calculateGrassGrowth(farmer);
		evaluateRestartState(farmer);
		evaluateFertilizingState(farmer);
		evaluateSoilImage(farmer);
		evaluateSeason(farmer);
		evaluateDisease(farmer);
		evaluatePlantImage(farmer);
		calculateDiggingCoefficient(farmer);
		farmer.save();
	}

	public void calculateFertalizing(Farmer farmer) {
		FertilizeService fertService = new FertilizeServiceImpl();
		if ((farmer.gameDate.dayOrder % 8) == 0) {
			try {
				farmer.field.plantation.needN = fertService
						.checkNeedOfN(farmer);
				farmer.field.plantation.needP = fertService
						.checkNeedOfP(farmer);
				farmer.field.plantation.needK = fertService
						.checkNeedOfK(farmer);
				farmer.field.plantation.needCa = fertService
						.checkNeedOfCa(farmer);
				farmer.field.plantation.needB = fertService
						.checkNeedOfB(farmer);
				farmer.field.plantation.needMg = fertService
						.checkNeedOfMg(farmer);
				farmer.field.plantation.needZn = fertService
						.checkNeedOfZn(farmer);
				farmer.field.plantation.save();
			} catch (NotSuchItemException e) {
				e.printStackTrace();
			}
		}
	}

	public void evaluateIce(Farmer farmer) {
		if (farmer.gameDate.heavyRain > 0.2) {
			// Disease
		}
	}

	public int seasionLevelSoilImage(int season_level) {
		if (season_level == 3 || season_level == 4) {
			return 1;
		}
		return season_level;
	}

}
