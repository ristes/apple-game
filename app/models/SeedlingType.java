package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * The characteristics of the seedlings used for planting.
 * 
 * @author ristes
 * 
 */
@Entity
public class SeedlingType extends Model {

	/**
	 * Minimal distance between the crown and the last tree limb for this
	 * seedling type
	 */
	public int fromHeight;

	/**
	 * Maximal distance between the crown and the last tree limb for this
	 * seedling type
	 */
	public int toHeight;

	public String description;

}
