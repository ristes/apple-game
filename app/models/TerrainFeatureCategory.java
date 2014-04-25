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
	 * If set, there must be defined at least one feature from this category for
	 * the terrain
	 */
	public boolean required;

	/**
	 * If set, the feature will be displayed without terrain analysis
	 */
	public boolean visible;

	/**
	 * Description of this feature
	 */
	public String description;

	@OneToMany(mappedBy = "category")
	public List<TerrainFeature> fetures;

}
