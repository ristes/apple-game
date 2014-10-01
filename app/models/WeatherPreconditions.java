package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 
 * Weather preconditions for decease occurrence
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="weatherpreconditions")
public class WeatherPreconditions extends Model {

	/**
	 * The range of weather conditions for decease occurrence. Concatenated
	 * intervals. Example: "[2-3)(4-6](6-7)[7-8]"
	 */
	public String occuenceRange;

	/**
	 * How this precondition changes the threshold for decease occurrence. If
	 * positive, the decease is less likely to occur.
	 */
	public int thresholdChange;

	/**
	 * For which weather type is this precondition and the given occurrence
	 * ranges
	 */
	@ManyToOne
	public WeatherType weather;

	/**
	 * For which decease is this precondition
	 */
	@ManyToOne
	public Disease decease;
}
