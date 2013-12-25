package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * This class defines how the terrain features impact on the yield
 * 
 * @author ristes
 * 
 */
@Entity
public class TerrainYieldImpact extends Model {

	/**
	 * The lost amount of yield quantity per unit of plantation, if the terrain
	 * feature is inadequate. Always positive value, subtracted from the
	 * {@link Yield} quantity.
	 */
	public int quantityImpact;

	@ManyToOne
	public TerrainFeature feature;

	@ManyToOne
	public Yield yield;

}
