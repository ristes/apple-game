package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="farmer_badges")
public class FarmerBadges extends Model{

	
	public int year;
	
	@ManyToOne
	public Farmer farmer;
	
	@ManyToOne
	public Badges badge;
	
}
