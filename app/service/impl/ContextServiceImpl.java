package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import models.Day;
import models.Farmer;
import service.ContextService;
import service.FertilizeService;
import service.ServiceInjector;
import dto.C;
import exceptions.NotSuchItemException;

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
		if (!ServiceInjector.fieldService.hasDropSystem(farmer)) {
			ServiceInjector.humidityGroovesService.calculateGroovesVarianceImpact(farmer);
			result = ServiceInjector.humidityGroovesService.varianceBrazdi(farmer);
		} else {
			ServiceInjector.humidityDropsService.calculateDropsVarianceImpact(farmer);
			result = ServiceInjector.humidityDropsService.varianceDrops(farmer);
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

	public Double calculateRainForPrevDays(Farmer farmer, Integer days) {
		Double result = 0.0;
		for (int i = 0; i < days; i++) {
			long dayOrder = farmer.gameDate.dayOrder - days + i;
			Day prevDay = Day.find("byDayOrder", dayOrder).first();
			if (prevDay != null) {
				Calendar c = Calendar.getInstance();

				Integer randm = ServiceInjector.randomGeneratorService.random(0.0, 7.0).intValue();
				result += rainCoefForMonth(c.get(Calendar.MONTH)) * randm;
			}

		}
		return result;
	}

	public void calculateCumulatives(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		Day today = farmer.gameDate;
		if (today.weatherType.id.equals(C.WEATHER_TYPE_RAINY)) {
			Integer randm = ServiceInjector.randomGeneratorService.random(0.1, 10.0).intValue();
			farmer.deltaCumulative += rainCoefForMonth(c.get(Calendar.MONTH))
					* randm;
		}
		farmer.rain_values = calculateRainForPrevDays(farmer, 5);

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
		farmer.save();
	}

	public void calculateGrassGrowth(Farmer farmer) {
		farmer.grass_growth += 0.2;
	}

	public void calculateDiggingCoefficient(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		int level = ServiceInjector.humidityService.humidityLevel(farmer);
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
		int hum_level = ServiceInjector.humidityService.humidityLevelForSoil(ServiceInjector.humidityService
				.humidityLevel(farmer));
		int plowing_level = ServiceInjector.landTreatmanService.plowingLevel(farmer);
		int digging_level = ServiceInjector.landTreatmanService.diggingLevel(farmer);
		int season_level = seasionLevelSoilImage(ServiceInjector.dateService
				.season_level(farmer));
		String tile_name = folder_path + name_prefix + separator + hum_level
				+ separator + plowing_level + separator + digging_level
				+ separator + season_level + extension;

		farmer.soil_url = tile_name;
	}


	public void evaluatePlantState(Farmer farmer) {
		
		farmer.plant_ajdaret = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.AJDARET);
		farmer.plant_crven_delishes = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.CRVEN_DELISHES);
		farmer.plant_zlaten_delishes = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.ZLATEN_DELISHES);
		farmer.plant_jonalgold = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.JONALGOLD);
		farmer.plant_greni_smit = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.GRENI_SMIT);
		farmer.plant_mucu = ServiceInjector.growingService.evaluatePlantImage(farmer,ContextService.MUCU);
	}

	public void evaluateSeason(Farmer farmer) {
		farmer.season_level = ServiceInjector.dateService.season_level(farmer);
		farmer.month_level = ServiceInjector.dateService.monthLevel(farmer.gameDate.date);
	}

	public void evaluateDisease(Farmer farmer) {
		// check for diseases every 5 days triggered by the farmer luck
		// in winter the diseases does not occure
		farmer.hasNewDisease = false;
		if (farmer.season_level !=ServiceInjector.dateService.SEASON_WINTER && farmer.season_level!=ServiceInjector.dateService.SEASON_AUTUMN) {
			if (farmer.gameDate.dayOrder % 5 == 0) {
				ServiceInjector.diseaseService.diseases(farmer);
			}
		}
	}

	public void evaluateRestartState(Farmer farmer) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date) >= 2) {
			if (month == Calendar.OCTOBER && day == 31) {
				triggerNewSeasonEvents(farmer);
			}
		}
	}
	
	public void triggerNewSeasonEvents(Farmer farmer) {
		farmer.isNewSeason = true;
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.ecologist(farmer));
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.irrigator(farmer));
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.fertilizer(farmer));
		farmer.productQuantity = (int) Math.round(ServiceInjector.yieldService
				.calculateYield(farmer));
		farmer.eco_points = 100;
		farmer.irrigation_misses = 0;
	}

	public void evaluateFertilizingState(Farmer farmer) {
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month == Calendar.OCTOBER && day == 1
				&& ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date) > 1) {
			FertilizeService fertService = new FertilizeServiceImpl();
			fertService.finalEvaluationFertilizer(farmer);
		}
	}

	public void calculateLuck(Farmer farmer) {
		if (farmer.gameDate.dayOrder % 5 == 0) {
			farmer.luck = generateLuck(farmer);
		}
	}

	
	public void onLoadEvaluateState(Farmer farmer) {
		evaluateSeason(farmer);
		evaluatePlantState(farmer);
		evaluateFridgesState(farmer);
		evaluateFertilizingState(farmer);
		evaluateSoilImage(farmer);
		evaluateApplesInStock(farmer);
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
		evaluatePlantState(farmer);
		evaluateFridgesState(farmer);
		calculateDiggingCoefficient(farmer);
		evaluateApplesInStock(farmer);
		evaluateUV(farmer);
		evaluateIce(farmer);
		farmer.save();
	}

	public void calculateFertalizing(Farmer farmer) {
		if ((farmer.gameDate.dayOrder % 8) == 0) {
			try {
				farmer.needN = ServiceInjector.fertilizeService
						.checkNeedOfN(farmer);
				farmer.needP = ServiceInjector.fertilizeService
						.checkNeedOfP(farmer);
				farmer.needK = ServiceInjector.fertilizeService
						.checkNeedOfK(farmer);
				farmer.needCa = ServiceInjector.fertilizeService
						.checkNeedOfCa(farmer);
				farmer.needB = ServiceInjector.fertilizeService
						.checkNeedOfB(farmer);
				farmer.needMg = ServiceInjector.fertilizeService
						.checkNeedOfMg(farmer);
				farmer.needZn = ServiceInjector.fertilizeService
						.checkNeedOfZn(farmer);
				farmer.field.plantation.save();
			} catch (NotSuchItemException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void evaluateIce(Farmer farmer) {
		ServiceInjector.iceService.impactLowTemp(farmer);
	}

	public int seasionLevelSoilImage(int season_level) {
		if (season_level == 3 || season_level == 4) {
			return 1;
		}
		return season_level;
	}

	@Override
	public void setAndCheckLastLoginDate(Farmer farmer) {
		
		Long last = System.currentTimeMillis();
		if (farmer.lastLogIn!=null) {
			last = farmer.lastLogIn.getTime();
		}
		ServiceInjector.thiefService.checkThiefProb(farmer);
		farmer.lastLogIn = new Date(System.currentTimeMillis());
		farmer.save();
	}
	
	public void evaluateApplesInStock(Farmer farmer) {
		farmer.apples_in_stock = ServiceInjector.fridgeService.getTotalApplesInStock(farmer);
		
	}
	
	public void evaluateFridgesState(Farmer farmer) {
		ServiceInjector.fridgeService.checkApplesState(farmer);
	}
	
	public String evaluateTip(Farmer farmer) {
		return ServiceInjector.tipService.randomTip(ServiceInjector.tipService.tipgenerator(farmer));
	}

	@Override
	public void evaluateUV(Farmer farmer) {
		ServiceInjector.uvService.impact(farmer);
		
	}

	@Override
	public void evaluateLowTemps(Farmer farmer) {
		ServiceInjector.iceService.impactLowTemp(farmer);
	}

	

}
