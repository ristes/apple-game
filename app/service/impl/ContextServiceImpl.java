package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import play.i18n.Messages;
import models.Day;
import models.Farmer;
import models.PlantType;
import models.Plantation;
import models.RottenApples;
import service.ContextService;
import service.DateService;
import service.FarmerService;
import service.FertilizeService;
import service.ServiceInjector;
import dto.C;
import dto.ResumeMessageDto;
import exceptions.NotSuchItemException;

public class ContextServiceImpl implements ContextService {

	public void calculateCumulatives(Farmer farmer) {
		ServiceInjector.humidityService.humidityGainsOnDailyLevel(farmer);
		ServiceInjector.humidityService.humidityLooses(farmer);
		// do not allow humidity above the max
		ServiceInjector.humidityService.regulateHumidityLimits(farmer);
		farmer.save();
	}

	public void evaluateSoilImage(Farmer farmer) {
		String folder_path = "/public/images/game/soil_types/";
		String name_prefix = "";
		String separator = "";
		String extension = ".png";
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		if (c.get(Calendar.MONTH) == Calendar.DECEMBER
				|| (c.get(Calendar.MONTH) < Calendar.MARCH && c.get(Calendar.DAY_OF_MONTH) <= 31)) {
			farmer.soil_url = "/public/images/game/soil_types/soil-snow.png";
			farmer.grass_growth = 0.0;
			farmer.digging_coef = 0.0;
			return;
		}
		int hum_level = ServiceInjector.humidityService
				.humidityLevelForSoil(ServiceInjector.humidityService.humidityLevel(farmer));
		int plowing_level = ServiceInjector.landTreatmanService.plowingLevel(farmer);
		int digging_level = ServiceInjector.landTreatmanService.diggingLevel(farmer);
		int season_level = seasionLevelSoilImage(ServiceInjector.dateService.season_level(farmer));
		String tile_name = folder_path + name_prefix + separator + hum_level + separator + plowing_level + separator
				+ digging_level + separator + season_level + extension;

		farmer.soil_url = tile_name;
	}

	public void evaluatePlantState(Farmer farmer) {

		farmer.plant_ajdaret = ServiceInjector.growingService.evaluatePlantImage(farmer, ContextService.AJDARET);
		farmer.plant_crven_delishes = ServiceInjector.growingService.evaluatePlantImage(farmer,
				ContextService.CRVEN_DELISHES);
		farmer.plant_zlaten_delishes = ServiceInjector.growingService.evaluatePlantImage(farmer,
				ContextService.ZLATEN_DELISHES);
		farmer.plant_jonalgold = ServiceInjector.growingService.evaluatePlantImage(farmer, ContextService.JONALGOLD);
		farmer.plant_greni_smit = ServiceInjector.growingService.evaluatePlantImage(farmer, ContextService.GRENI_SMIT);
		farmer.plant_mucu = ServiceInjector.growingService.evaluatePlantImage(farmer, ContextService.MUCU);
	}

	public void evaluateSeason(Farmer farmer) {
		farmer.season_level = ServiceInjector.dateService.season_level(farmer);
		farmer.month_level = ServiceInjector.dateService.monthLevel(farmer.gameDate.date);
		farmer.year_level = ServiceInjector.dateService
				.evaluateYearLevel(ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
		farmer.year_order = ServiceInjector.dateService
				.evaluateYearOrder(ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
		ServiceInjector.gameEndService.evaluate(farmer);
	}

	public void evaluatePrevDaysRainValue(Farmer farmer) {
		farmer.rain_values = ServiceInjector.weatherService.getPrevDaysValue(farmer, 5);
	}

	public void evaluateRestartState(Farmer farmer) {

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date) >= DateService.YEAR_ORDINARY_SECOND) {
			if (month == Calendar.OCTOBER && day == 31) {
				triggerNewSeasonEvents(farmer);
			}
		}
	}

	public void triggerNewSeasonEvents(Farmer farmer) {
		farmer.isNewSeason = true;
		evaluateEndOfTestSubstate(farmer);
		int recolte = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.ecologist(farmer));
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.irrigator(farmer));
		ServiceInjector.farmerService.collectBadge(farmer, ServiceInjector.badgesService.fertilizer(farmer));

		farmer.productQuantity = (int) Math.round(ServiceInjector.yieldService.calculateYield(farmer));

		Integer moneyEarned = ServiceInjector.resumeService.calcMoneyEarned(farmer, recolte);
		Integer applesHarvested = ServiceInjector.resumeService.calculateYield(farmer, recolte);
		ServiceInjector.rankingService.savePoints(farmer, (int) farmer.getEco_points(), applesHarvested, moneyEarned);
		ServiceInjector.ecoPointsService.restart(farmer);
		farmer.irrigation_misses = 0;
	}

	public void evaluateFertilizingState(Farmer farmer) {
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month == Calendar.OCTOBER && day == 1
				&& ServiceInjector.dateService.evaluateYearLevel(farmer.gameDate.date) > 1) {
			ServiceInjector.fertilizeService.finalEvaluationFertilizer(farmer);
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
		evaluatePrevDaysRainValue(farmer);
		evaluateDisease(farmer);
		evaluatePlantState(farmer);
		evaluateFridgesState(farmer);
		calculateDiggingCoefficient(farmer);
		evaluateRottenApples(farmer);
		evaluateApplesInStock(farmer);

		farmer.save();
	}

	private void calculateLuck(Farmer farmer) {
		ServiceInjector.luckService.generateLuck(farmer);
	}

	private void calculateDiggingCoefficient(Farmer farmer) {
		ServiceInjector.landTreatmanService.evalDiggingCoefs(farmer);

	}

	private void evaluateDisease(Farmer farmer) {
		ServiceInjector.diseaseService.evaluateDiseases(farmer);

	}

	private void calculateGrassGrowth(Farmer farmer) {
		ServiceInjector.grassGrowthService.inc(farmer);

	}

	public void calculateFertalizing(Farmer farmer) {
		ServiceInjector.fertilizeService.evalFertilizingState(farmer);
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
		if (farmer.lastLogIn != null) {
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

	public void evaluateRottenApples(Farmer farmer) {
		List<PlantType> plantTypes = ServiceInjector.plantTypeService.ownedByFarmer(farmer);
		for (PlantType pt : plantTypes) {
			if (ServiceInjector.harvestService.isAfterHarvestingPeriod(farmer, pt.id)) {
				Integer year = ServiceInjector.dateService.fridgerecolteyear(farmer.gameDate.date);
				Integer recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
				if (recolteYear > 2021) {
					if (!ServiceInjector.yieldService.areApplesByTypeAndYearHarvested(farmer, pt, year)) {
						if (ServiceInjector.rottenApplesService.getByFarmerYearAndPlantType(farmer, year, pt)
								.size() == 0) {
							String message = Messages.get("lost_quantity_rotten", pt.name);
							ServiceInjector.infoTableService.createT1(farmer, message, pt.imageurl);
							RottenApples rottenApples = new RottenApples();
							rottenApples.setFarmer(farmer);
							rottenApples.setPlantType(pt);
							rottenApples.setYear(year);
							rottenApples.save();
						}
					}
				}
			}
		}
	}

	public String evaluateTip(Farmer farmer) {
		return ServiceInjector.tipService.randomTip(farmer, ServiceInjector.tipService.tipgenerator(farmer));
	}

	public void evaluateEndOfTestSubstate(Farmer farmer) {
		if (farmer.subState.equals(FarmerService.SUBSTATE_TEST_PERIOD)) {
			farmer.subState = FarmerService.SUBSTATE_FINISHED_TEST_PERIOD;
		}
	}

	@Override
	public void evaluateLowTemps(Farmer farmer) {
		ServiceInjector.iceService.impactLowTemp(farmer);
	}

}
