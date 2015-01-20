package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "plant_type_deadline")
public class PlantTypeDeadine extends Model {
	
	public Date date;

	@ManyToOne
	public PlantType plantType;
	
	@ManyToOne
	public FridgeType fridge;



}
