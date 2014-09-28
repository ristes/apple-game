package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * The definition for the items
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "item")
public class Item extends Model {

	/**
	 * The name of the item
	 */
	public String name;

	public String description;

	@ManyToOne
	public ItemType type;

	/**
	 * The price for the item
	 */
	public int price;

	/**
	 * JSON object that describes the object
	 */
	public String metadata;

	public String imageurl;

	/**
	 * How this item affects the ecology. Ranges from 0 to 10, where 0 is the
	 * best, with no pollution at all, and 10 is high pollution
	 */
	@Range(min = 0, max = 10)
	public int pollutionCoefficient;

	@ManyToOne
	public SpriteImage image;

	/**
	 * Where can we buy the item
	 */
	@ManyToOne
	public Store store;

	@OneToMany(mappedBy = "fertilizer")
	public List<FertilizationOperation> fertilizationOperations;

	/**
	 * If the item function as attachment of other item. If the other item is
	 * not owned, this can't be acquired.
	 */
	@ManyToOne
	public Item attachedTo;

	/**
	 * The operation which is executed with this item
	 */
	@ManyToOne
	public Operation operation;

	/**
	 * The list of additional items that can be attached to this item
	 */
	@OneToMany(mappedBy = "attachedTo")
	public List<Item> attachments;

	public String toString() {
		return name;
	}

}
