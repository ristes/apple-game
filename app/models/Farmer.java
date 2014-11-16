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
import javax.persistence.Table;
import javax.swing.text.Position.Bias;

import org.apache.commons.collections.map.HashedMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import controllers.DeseasesExpertSystem;
import controllers.FertilizationController;
import controllers.HumidityController;
import controllers.IrrigationController;
import controllers.LandTreatmanController;
import controllers.WeatherController;
import controllers.YieldController;
import dto.C;
import dto.DiseaseOccurenceProb;
import dto.DiseasesOccured;
import dto.FertilizationItem;
import play.db.jpa.Model;
import service.DateService;
import service.GrowingService;
import service.impl.DateServiceImpl;
import service.impl.GrowingServiceImpl;
import utils.GameUtils;

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
	public int season_level;

	public double eco_points = 100;

	public int apples_in_stock = 0;
	
	private int apples_in_na_fridge;
	
	private int apples_in_ca_fridge;

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
	
	public void addToApples_in_stock(int apples) {
		
		int rest_capacity_ca = capacityCAFridges - this.apples_in_ca_fridge;
		if (rest_capacity_ca>=apples) {
			this.apples_in_ca_fridge += apples;
		} else {
			this.apples_in_ca_fridge = capacityCAFridges;
			int rest_app_ca = apples - rest_capacity_ca;
			int rest_capacity_na = capacityNAFridges - this.apples_in_na_fridge;
			if (rest_capacity_na>=rest_app_ca) {
				this.apples_in_na_fridge += rest_app_ca;
			} else {
				this.apples_in_na_fridge = capacityNAFridges;
				int rest_app_na = rest_app_ca - rest_capacity_na;
				this.apples_in_stock += rest_app_na;
			}
		}
		
		
	}
	
	public int sumOfApples() {
		return this.apples_in_stock + this.apples_in_na_fridge + this.apples_in_ca_fridge;
	}

	public int getApples_in_na_fridge() {
		return apples_in_na_fridge;
	}

	public int getApples_in_ca_fridge() {
		return apples_in_ca_fridge;
	}

	

}
