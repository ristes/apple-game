package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * The player in the game
 * 
 * @author ristes
 * 
 */
@Entity
public class Farmer extends Model {

	/**
	 * How much money does the player have
	 */
	public int balans;

	/**
	 * The quantity of the product he has gained, and haven't sold yet
	 */
	public int producatQuantity;

	/**
	 * The date for the player in the game
	 */
	public Date gameDate;

	/**
	 * The fields he owns
	 */
	@OneToMany(mappedBy = "owner")
	public List<Field> plantations;

	/**
	 * The items he owns
	 */
	@OneToMany(mappedBy = "ownedBy")
	public List<ItemInstance> boughtItems;

	/**
	 * The operations he has executed
	 */
	@OneToMany(mappedBy = "executor")
	public List<ExecutedOperation> executedOperations;

}
