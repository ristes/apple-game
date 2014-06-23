package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * The maximum apple yield categorization of each combination of base, type and
 * terrain
 * 
 * @author ristes
 * 
 */
@Entity
public class Yield extends Model {

	/**
	 * The maximum expected quantity, obtained with perfect care and conditions
	 */
	public int quantity;
	
	@ManyToOne
	public Farmer farmer;
	
	public Integer year;

	/**
	 * The apple base
	 */
	@ManyToOne
	public Base base;

	/**
	 * The apple type
	 */
	@ManyToOne
	public SeedlingType type;

}
