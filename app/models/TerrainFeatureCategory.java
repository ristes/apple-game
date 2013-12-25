package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * Group the terrain features by the thing they are describing
 * 
 * @author ristes
 * 
 */
@Entity
public class TerrainFeatureCategory extends Model {

	/**
	 * The name of the category
	 */
	public String name;

	/**
	 * Description of this feature
	 */
	public String description;

	@OneToMany(mappedBy = "category")
	public List<TerrainFeature> fetures;

}
