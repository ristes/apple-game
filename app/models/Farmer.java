package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.swing.text.Position.Bias;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
		if (luck<(avg-stand_dev)) {
			luck = avg-stand_dev;
		}
		return luck;
	}

	public Boolean isSameYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(gameDate.date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 1);
		if (date.after(c.getTime())) {
			c.set(Calendar.DAY_OF_MONTH, 31);
			c.set(Calendar.MONTH, 12);
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
		calculateCumulatives();
		calculateLuck(gameDate);
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

	public static Farmer buildInstance(String username, String password) {
		Farmer farmer = new Farmer();
		farmer.username = username;
		farmer.password = password;
		Day start = Day.find("dayOrder", 0l).first();
		farmer.gameDate = start;
		farmer.balans = 1000000;
		farmer.eco_points = 100;
		farmer.cumulativeHumidity = 20d;
		farmer.cumulativeLeafHumidity = 15d;
		farmer.luck_dev = 0.3;
		farmer.luck_avg = 0.7;
		Double luck = farmer.generateLuck();
		farmer.luck = luck;
		farmer.save();
		return farmer;
	}

}
