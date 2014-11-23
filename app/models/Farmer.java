package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import play.db.jpa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The player in the game
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "farmer")
public class Farmer extends Model {

	public String username;

	@JsonIgnore
	public String password;

	public String currentState;

	public Double luck;

	public Double luck_dev;

	public Double luck_avg;
	
	public Double rain_values;
	
	public Date lastLogIn;

	@OneToMany(mappedBy = "farmer")
	public List<Yield> yields;

	/**
	 * How much money does the player have
	 */
	public double balans;

	/**
	 * season labeled in WeatherController
	 */
	public Integer season_level;

	public double eco_points = 100;

	
	@Transient
	public Integer apples_in_stock = 0;
	

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
	public String plant_url_gold;
	public String plant_url_green;
	
	@Transient
	public String plant_ajdaret;
	
	@Transient
	public String plant_crven_delishes;
	
	@Transient
	public String plant_zlaten_delishes;
	
	@Transient
	public String plant_jonalgold;
	
	@Transient
	public String plant_mucu;
	
	@Transient
	public String plant_greni_smit;
	
	public Integer plants_green;
	public Integer plants_red;
	public Integer plants_gold;
	
	
	public Integer capacityNAFridges;
	
	public Integer capacityCAFridges;

	/**
	 * flags that represents the restarted state of the farmer if set to false,
	 * the farmer is deactivated
	 */
	public Boolean is_active;

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

	public String fb_access_token;

	public String name;

	public String surname;

	public String email;

	public String picture;

	public boolean sharedApp;

	public Double cumulativeRains;

	public String tips;
	
	/**
	 * need of minerals
	 */
	public Boolean needN;
	public Boolean needP;
	public Boolean needK;
	public Boolean needCa;
	public Boolean needB;
	public Boolean needZn;
	public Boolean needMg;
	
	/**
	 * The items he owns
	 */
	@OneToMany(mappedBy = "ownedBy")
	public List<ItemInstance> boughtItems;

	public Double getLuck() {
		return luck;
	}

	public Double getBalance() {
		return balans;
	}

	public void setBalance(Double balance) {
		this.balans = balance;
	}

	public int getApples_in_stock() {
		return apples_in_stock;
	}

	public void setApples_in_stock(int apples_in_stock) {
		this.apples_in_stock = apples_in_stock;
		
	}
	

}
