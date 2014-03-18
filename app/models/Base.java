package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

/**
 * The way the trees are planted i.e. M9, M27 etc.
 * 
 * A lot of characteristics depends on this choice
 * 
 * @author ristes
 * 
 */
@Entity
public class Base extends Model {

	/**
	 * The price to buy the corresponding base for unit of plantation
	 */
	public int price;

	/**
	 * The base may require some items like support handles etc.
	 */
	@ManyToMany
	public List<Item> requiredItems;

	/**
	 * Which types can be planted using this base
	 */
	@ManyToMany(mappedBy = "plantOn")
	public List<PlantType> allowedPlantationFor;

}
