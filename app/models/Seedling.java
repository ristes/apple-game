package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * 
 * The type of seedlings used for planting
 * 
 * @author ristes
 * 
 */
@Entity
public class Seedling extends Model {

	/**
	 * The price to buy these seedlings per unit of plantation
	 */

	public int price;
	/**
	 * The type of the appes
	 */
	@ManyToOne
	public PlantType type;

	/**
	 * The seedling type
	 */
	@ManyToOne
	public SeedlingType seedlingType;

	@Override
	public String toString() {
		return String.format("[%d] %s (%s)", id, type.name,
				seedlingType.description);
	}

}
