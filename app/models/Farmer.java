package models;

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

import controllers.IrrigationController;
import controllers.LandTreatmanController;
import dto.C;
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

	/**
	 * How much money does the player have
	 */
	public int balans;

	public int eco_points = 100;

	public int apples_in_stock = 0;

	/**
	 * The quantity of the product he has gained, and haven't sold yet
	 */
	@JsonIgnore
	public int producatQuantity;

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
	 * digging coefficient represents the need of digging of the plantation
	 * 1 - (not need) it's clear
	 * 2 - (medium) little grass
	 * 3 - (high) lot of grass
	 */
	public Double digging_coef;
	
	
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
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 9);
		if (date.after(c.getTime())) {
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH, 9);
			if (c.before(date)) {
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

	public void calculateCumulatives() {
		Day today = gameDate;
		cumulativeLeafHumidity = gameDate.humidityOfLeaf.doubleValue();
		switch (today.weatherType.id.intValue()) {
		case 1:
			if (today.humidity > 50) {
				cumulativeHumidity = cumulativeHumidity
						- (cumulativeHumidity / 300) * today.tempHigh;
			} else {
				cumulativeHumidity = cumulativeHumidity
						- (cumulativeHumidity / 300) * today.tempHigh
						+ today.humidity / 20;
			}

			break;
		case 2:
			if (today.humidity > 50) {
				cumulativeHumidity = cumulativeHumidity + today.humidity / 25;
			} else {
				cumulativeHumidity = cumulativeHumidity - today.humidity / 5;
			}
			break;
		case 3:
			cumulativeHumidity = cumulativeHumidity + today.humidity / 8;
			break;
		case 4:
			cumulativeHumidity = cumulativeHumidity + today.humidity / 20;
			break;
		default:
			break;
		}
		this.cumulativeHumidity = cumulativeHumidity;
	}

	public void calculateGrassGrowth() {
		grass_growth += 0.2;
	}
	
	public void calculateDiggingCoefficient() {
		if (cumulativeHumidity>1500) {
			digging_coef += 0.2;
		} else if (cumulativeHumidity >= 1000) {
			digging_coef += 0.1;
		} else if (cumulativeHumidity < 1000) {
			digging_coef += 0.05;
		}
	}

	public void evaluateSoilImage(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (c.get(Calendar.MONTH) == 11 || (c.get(Calendar.MONTH) < 3 && c.get(Calendar.DAY_OF_MONTH) <= 31)) {
			soil_url = C.soil_urls[C.soil_with_snow];
			grass_growth = 0.0;
			digging_coef = 0.0;
			return;
		}
		
		int hum_level = IrrigationController.humidityLevel(this);
		int plowing_level = LandTreatmanController.plowingLevel(this);
		int digging_level = LandTreatmanController.diggingLevel(this);
		if (hum_level == 3 && plowing_level == 3) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_high_grass_high];
		} else if (hum_level == 3 && plowing_level == 2) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_high_grass_medium];
		} else if (hum_level == 3 && plowing_level == 1) {
			soil_url = C.soil_urls[C.soil_irrigated_high_plowing_normal];
		} else if (hum_level == 2 && plowing_level == 3) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_high_grass_high];
		} else if (hum_level == 2 && plowing_level == 2) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_normal];
		} else if (hum_level == 2 && plowing_level == 1) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_normal];
		} else if (hum_level == 1 && plowing_level == 3) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_high_grass_high];
		} else if (hum_level == 1 && plowing_level == 2) {
			soil_url = C.soil_urls[C.soil_irrigated_brazdi_high_grass_medium];
		} else if (hum_level == 1 && plowing_level == 1) {
			soil_url = C.soil_urls[C.soil_normal];
		}
	}

	public void evaluateState() {
		calculateCumulatives();
		calculateLuck(gameDate);
		calculateGrassGrowth();
		evaluateSoilImage(gameDate.date);
		calculateDiggingCoefficient();
	}
	
	public static Farmer buildInstance(String username, String password) {
		Farmer farmer = new Farmer();
		farmer.username = username;
		farmer.password = password;
		Day start = Day.find("dayOrder", 243l).first();
		farmer.gameDate = start;
		farmer.balans = 1000000;
		farmer.eco_points = 100;
		farmer.cumulativeHumidity = 20d;
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
