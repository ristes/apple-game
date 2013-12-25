package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * Store from which the items can be bought
 * 
 * @author ristes
 * 
 */
@Entity
public class Store extends Model {

	public String name;

	/**
	 * The items that store contains
	 */
	@OneToMany(mappedBy = "store")
	List<Item> items;

}
