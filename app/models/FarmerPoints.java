package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="farmer_points")
public class FarmerPoints extends Model{
	
	@ManyToOne
	private Farmer farmer;
	
	private Integer year;
	
	private Integer ecoPoints;
	
	private Integer harvestQuantity;
	
	private Integer moneyEarned;
	
	private Integer totalPoints;

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getEcoPoints() {
		return ecoPoints;
	}

	public void setEcoPoints(Integer ecoPoints) {
		this.ecoPoints = ecoPoints;
	}

	public Integer getHarvestQuantity() {
		return harvestQuantity;
	}

	public void setHarvestQuantity(Integer harvestQuantity) {
		this.harvestQuantity = harvestQuantity;
	}

	public Integer getMoneyEarned() {
		return moneyEarned;
	}

	public void setMoneyEarned(Integer moneyEarned) {
		this.moneyEarned = moneyEarned;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	

}
