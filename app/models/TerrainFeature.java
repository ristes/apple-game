package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * The terrain feature of a given category
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="terrainfeature")
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

	@ManyToOne
	public TerrainAnalysis analysis;

}
