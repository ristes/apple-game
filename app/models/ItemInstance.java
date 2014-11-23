package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * Instance of an item owned by (bought) farmer
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "iteminstance")
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

	public Double quantity;

	public Integer year;

}
