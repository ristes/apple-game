package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

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
	
	@JsonIgnore
	public Double luck;

	/**
	 * How much money does the player have
	 */
	public int balans;
	
	public int eco_points=100;
	
	public int apples_in_stock=0;

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
	
	public void generateLuck() {
		luck = GameUtils.random(0.5,1.0,0.2);
	}
	
	public Boolean isSameYear(Date date) {
		Calendar c= Calendar.getInstance();
		c.setTime(gameDate.date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 1);
		if (date.after(c.getTime())) {
			c.set(Calendar.DAY_OF_MONTH,31);
			c.set(Calendar.MONTH,12);
			if (c.before(date)) {
				return true;
			}
		}
		return false;
	}

}
