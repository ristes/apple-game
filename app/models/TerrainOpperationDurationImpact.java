package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * 
 * This class models the impact of the terrain feature to the duration of some
 * operations
 * 
 * @author ristes
 * 
 */
@Entity
public class TerrainOpperationDurationImpact extends Model {

	/**
	 * Time added to the operation duration per unit of plantation if the
	 * terrain feature is inadequate (percents of day)
	 */
	public int durationImpact;

	/**
	 * The feature that changes the duration
	 */
	@ManyToOne
	public TerrainFeature feature;

	/**
	 * The changing operation duration
	 */
	@ManyToOne
	public OperationDuration operationDuration;

}
