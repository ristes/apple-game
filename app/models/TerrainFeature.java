package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * The terrain feature of a given category
 * 
 * @author ristes
 * 
 */
@Entity
public class TerrainFeature extends Model {

	/**
	 * The value of the feature in the category
	 */
	public String value;

	/**
	 * The category of the feature i.e. the group of features in whch it belongs
	 * to
	 */
	@ManyToOne
	public TerrainFeatureCategory category;

	@ManyToMany(mappedBy = "features")
	public List<Terrain> terrains;

}
