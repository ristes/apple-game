package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="fridge")
public class Fridge extends Model{
	
	public Integer capacity;
	
	public Integer type;
	
	@ManyToOne
	public Farmer farmer;
	
	

}
