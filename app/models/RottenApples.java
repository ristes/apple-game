package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="rotten_apples")
public class RottenApples extends Model{
	
	private Integer year;
	
	@ManyToOne
	private Farmer farmer;
	
	@ManyToOne
	private PlantType plantType;
	
	private Integer quantity;
	
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Farmer getFarmer() {
		return farmer;
	}
	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}
	public PlantType getPlantType() {
		return plantType;
	}
	public void setPlantType(PlantType plantType) {
		this.plantType = plantType;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	

}
