package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * The initial threshold for decease occurrence and the impact to the yield
 * 
 * @author ristes
 * 
 */
@Entity
public class DeceaseImpact extends Model {

	/**
	 * The threshold for decease occurring. It overrides the default threshold
	 */
	public int threshold;

	/**
	 * how much the yield will be diminished with these decease (percents). Zero
	 * means no loss at all; 100 means no yield at all
	 */
	@Range(min = 0, max = 100)
	public int diminishingFactor;

	/**
	 * The apple type
	 */
	@ManyToOne
	public PlantType type;

	/**
	 * The apple base
	 */
	@ManyToOne
	public Base base;

	/**
	 * the decease
	 */
	@ManyToOne
	public Decease decease;

}
