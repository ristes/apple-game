package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * Instance of an item owned by (bought) farmer
 * 
 * @author ristes
 * 
 */
@Entity
public class ItemInstance extends Model {

	/**
	 * The type of the item
	 */
	@ManyToOne
	public Item type;

	/**
	 * Who owns the instance
	 */
	@ManyToOne
	public Farmer ownedBy;

}
