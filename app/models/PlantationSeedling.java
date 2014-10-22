package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 
 * The type of seedlings used for planting
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "plantation_seedling")
public class PlantationSeedling extends Model {

	/**
	 * The price to buy these seedlings per unit of plantation
	 */

	public int quantity;
	
	
	/**
	 * this percent gives information how many of trees planted on plantation are from this type of seedling
	 */
	public int percentOfPlantedArea;
	/**
	 * The type of the appes
	 */
	@ManyToOne
	public Plantation plantation;

	/**
	 * The seedling type
	 */
	@ManyToOne
	public Seedling seedling;

	@Override
	public String toString() {
		return String
				.format("[%d] (%s)", id, seedling.seedlingType.description);
	}

}
