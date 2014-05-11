package models;

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
	
	public Double luck;

	/**
	 * How much money does the player have
	 */
	public int balans;

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

}
