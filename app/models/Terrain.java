package models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

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
public class Terrain extends Model {

	/**
	 * The description for the terrain, shown in the process of buying of the
	 * field.
	 */
	public String description;

	@OneToOne
	public TerrainAnalysis analysis;

}
