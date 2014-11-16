package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 
 * The terrain is represented by a group of terrain features. It contains
 * exactly one feature from each category
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "terrain")
public class Terrain extends Model {

	public String name;
	/**
	 * The description for the terrain, shown in the process of buying of the
	 * field.
	 */
	public String description;

	public String imageurl;
	
	/*
	 * This coef represents the absorption of the soil. If it absorbs well the yield is  1.1, otherwise it is 1.0
	 * Praskasta has 1.1
	 */
	public Double yieldCoef;

	@OneToOne
	public TerrainAnalysis analysis;

}
