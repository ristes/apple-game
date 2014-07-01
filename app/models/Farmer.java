package models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.swing.text.Position.Bias;

import org.apache.commons.collections.map.HashedMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import controllers.DeseasesExpertSystem;
import controllers.FertilizationController;
import controllers.GrowController;
import controllers.HumidityController;
import controllers.IrrigationController;
import controllers.LandTreatmanController;
import controllers.WeatherController;
import controllers.YieldController;
import dao.DateDao;
import dto.C;
import dto.DiseaseOccurenceProb;
import dto.DiseasesOccured;
import dto.FertilizationItem;
import play.db.jpa.Model;
import utils.GameUtils;

/**
 * The player in the game
 * 
 * @author ristes
 * 
 */
@Entity
public class Farmer extends Model {

	public String username;

	@JsonIgnore
	public String password;

	public String currentState;

	public Double luck;

	public Double luck_dev;

	public Double luck_avg;

	@OneToMany(mappedBy = "farmer")
	public List<Yield> yields;

	/**
	 * How much money does the player have
	 */
	public int balans;

	/**
	 * season labeled in WeatherController
	 */
	public int season_level;

	public double eco_points = 100;

	public int apples_in_stock = 0;

	/**
	 * The quantity of the product he has gained, and haven't sold yet
	 */
	// @JsonIgnore
	public int productQuantity;

	/**
	 * The date for the player in the game
	 */

	@ManyToOne
	public Day gameDate;

	/**
	 * The fields he owns
	 */
	@OneToOne
	public Field field;

	public Double deltaCumulative;

	public Double cumulativeHumidity;
	public Double cumulativeLeafHumidity;

	public String soil_url;
	public String plant_url;

	/*
	 * coefficient of soil type influences the irrigation of the plants between
	 * 0 and 10 5 is medium and greater that 8 is high
	 */
	public Integer coef_soil_type;

	/**
	 * coefficient of grass growth in the field represents the need of plowing
	 */
	public Double grass_growth;

	/**
	 * digging coefficient represents the need of digging of the plantation 1 -
	 * (not need) it's clear 2 - (medium) little grass 3 - (high) lot of grass
	 */
	public Double digging_coef;
	
	
	public double subtractEcoPoints(double points) {
		eco_points -= points;
		if (eco_points<0) {
			eco_points = 0;
		}
		return eco_points;
	}

	/**
	 * The items he owns
	 */
	@OneToMany(mappedBy = "ownedBy")
	public List<ItemInstance> boughtItems;

	public Double getLuck() {
		return luck;
	}

	public Double generateLuck() {
		Random random = new Random();
		Double stand_dev = luck_dev;
		Double avg = luck_avg;
		luck = (random.nextGaussian() * stand_dev + avg);
		if (luck < (avg - stand_dev)) {
			luck = avg - stand_dev;
		}
		return luck;
	}

	public Boolean isSameYear(Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(gameDate.date);
		if (DateDao.isAfterNewYear(c.getTime())) {
			c.add(Calendar.YEAR, -1);
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 9);

		if (date.after(c.getTime())) {
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH, 9);
			if (date.before(c.getTime())) {
				return true;
			}
		}
		return false;
	}

	public Farmer gotoNextDay() {
		Day today = gameDate;
		List<Day> gameDates = Day.find("byDayOrder", gameDate.dayOrder + 1)
				.fetch();
		gameDate = gameDates.get(0);
		evaluateState();
		this.save();
		return this;
	}

	public void calculateLuck(Day gameDate) {
		if (gameDate.dayOrder % 5 == 0) {
			this.luck = generateLuck();
		}
	}

	public double calculateHumidityLooses() {
		double result = 0.0;
		if (!field.hasDropSystem(Farmer.this)) {

			productQuantity = HumidityController
					.brazdi_irrigation_delta_impact_quantity(Farmer.this);
			eco_points = HumidityController
					.brazdi_irrigation_delta_impact_eco_point(Farmer.this);
			result = HumidityController.varianceBrazdi(Farmer.this);
		} else {
			productQuantity = HumidityController
					.drops_irrigation_delta_impact_quantity(Farmer.this);
			eco_points = HumidityController
					.drops_irrigation_delta_impact_eco_point(Farmer.this);
			result = HumidityController.varianceDrops(Farmer.this);
		}
		return result;
	}

	public void calculateCumulatives() {
		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();
		Calendar c = Calendar.getInstance();
		c.setTime(gameDate.date);
		Day today = gameDate;
		if (today.weatherType.id == C.WEATHER_TYPE_RAINY) {
			double avg_rain = coefs.get(C.KEY_RAIN_COEFS).get(
					c.get(Calendar.MONTH));
			// deltaCumulative += coefs.get(C.KEY_DROPS_EVAP).get(
			// c.get(Calendar.MONTH))
			// * avg_rain;
			deltaCumulative += avg_rain;
		}

		if (gameDate.dayOrder % 8 == 0) {
			double variance = calculateHumidityLooses();
			cumulativeHumidity += variance;
			deltaCumulative = variance;
			Double min_hum = coefs.get(C.KEY_MIN_HUMIDITY).get(0);
			if (deltaCumulative < min_hum) {
				deltaCumulative = min_hum;
			}
		}
		// do not allow humidity above the max
		double max_humidity = coefs.get(C.KEY_MAX_HUMIDITY).get(0);
		if (deltaCumulative > max_humidity) {
			deltaCumulative = max_humidity;
		}
	}

	public void calculateGrassGrowth() {
		grass_growth += 0.2;
	}

	public void calculateDiggingCoefficient() {
		HashMap<String, ArrayList<Double>> coefs = HumidityController
				.load_hash();

		int level = HumidityController.humidityLevel(Farmer.this);
		if (level >= 3) {
			digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(3);
		} else if (level == 2) {
			digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(2);
		} else if (level == 1) {
			digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(1);
		} else if (level == 0) {
			digging_coef += coefs.get(C.KEY_DIGGING_COEF).get(0);
		}
	}

	public void evaluateSoilImage(Date date) {
		String folder_path = "/public/images/game/soil_types/";
		String name_prefix = "";
		String separator = "";
		String extension = ".png";
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (c.get(Calendar.MONTH) == 11
				|| (c.get(Calendar.MONTH) < 2 && c.get(Calendar.DAY_OF_MONTH) <= 31)) {
			soil_url = "/public/images/game/soil_types/soil-snow.png";
			grass_growth = 0.0;
			digging_coef = 0.0;
			return;
		}

		int hum_level = HumidityController
				.humidityLevelForSoil(HumidityController.humidityLevel(this));
		int plowing_level = LandTreatmanController.plowingLevel(this);
		int digging_level = LandTreatmanController.diggingLevel(this);
		int season_level = WeatherController
				.seasionLevelSoilImage(WeatherController
						.season_level(Farmer.this));
		String tile_name = folder_path + name_prefix + separator + hum_level
				+ separator + plowing_level + separator + digging_level
				+ separator + season_level + extension;

		soil_url = tile_name;
	}

	public void evaluatePlantImage() {
		plant_url = GrowController.evaluatePlantImage(Farmer.this);
	}

	public void evaluateSeason() {
		season_level = WeatherController.season_level(Farmer.this);
	}

	public void evaluateDisease() {
		// check for diseases every 5 days triggered by the farmer luck
		if (gameDate.dayOrder % 5 == 0) {
			DeseasesExpertSystem.diseases();
		}
	}

	public void evaluateRestartState() {
		Calendar c = Calendar.getInstance();
		c.setTime(gameDate.date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int year = c.get(Calendar.YEAR);
		if (WeatherController.evaluateYearLevel(year) >= 2) {
			if (month == 9 && day == 1) {
				productQuantity = (int) Math.round(YieldController.calculateYield());
				eco_points = 100;
			}
		}
	}
	
	public void evaluateFertilizingState() {
		Calendar c = Calendar.getInstance();
		c.setTime(gameDate.date);
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month == Calendar.SEPTEMBER && day==1 && WeatherController.evaluateYearLevel(year)>1) {
			FertilizationController.finalEvaluationFertilizer();
		}
	}

	public void evaluateState() {
		calculateCumulatives();
		calculateFertalizing();
		calculateLuck(gameDate);
		calculateGrassGrowth();
		evaluateRestartState();
		evaluateFertilizingState();
		evaluateSoilImage(gameDate.date);
		evaluateSeason();
		evaluateDisease();
		evaluatePlantImage();
		calculateDiggingCoefficient();
	}

	public void calculateFertalizing() {
		if ((gameDate.dayOrder % 8) == 0) {
			field.plantation.needN = FertilizationController
					.checkNeedOfN(Farmer.this);
			field.plantation.needP = FertilizationController
					.checkNeedOfP(Farmer.this);
			field.plantation.needK = FertilizationController
					.checkNeedOfK(Farmer.this);
			field.plantation.needCa = FertilizationController
					.checkNeedOfCa(Farmer.this);
			field.plantation.needB = FertilizationController
					.checkNeedOfB(Farmer.this);
			field.plantation.needMg = FertilizationController
					.checkNeedOfMg(Farmer.this);
			field.plantation.needZn = FertilizationController
					.checkNeedOfZn(Farmer.this);
			field.plantation.save();
		}
	}

	public static Farmer buildInstance(String username, String password) {
		Farmer farmer = new Farmer();
		farmer.username = username;
		farmer.password = password;
		Day start = Day.find("dayOrder", 243l).first();
		farmer.gameDate = start;
		farmer.balans = 1000000;
		farmer.eco_points = 100;
		farmer.deltaCumulative = 0.0;
		farmer.cumulativeHumidity = 0.0;
		farmer.cumulativeLeafHumidity = 15d;
		farmer.luck_dev = 0.3;
		farmer.luck_avg = 0.7;
		Double luck = farmer.generateLuck();
		farmer.luck = luck;
		farmer.plant_url = "/public/images/game/plant.png";
		farmer.soil_url = C.soil_urls[0];
		farmer.coef_soil_type = 1;
		farmer.grass_growth = 5.0;
		farmer.digging_coef = 1.0;

		farmer.save();
		return farmer;
	}

}
