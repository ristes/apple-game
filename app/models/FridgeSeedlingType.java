package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;


@Entity
@Table(name="fridge_seedlingtype")
public class FridgeSeedlingType extends Model{
	
	public Long current_state;
	
	@ManyToOne
	public Fridge fridge;
	
	@ManyToOne
	public PlantType plantType;
	

}
