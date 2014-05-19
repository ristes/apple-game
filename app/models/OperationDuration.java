package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * Defines the time of operation duration based on the terrain, the base and the
 * used tool (item)
 * 
 * @author ristes
 * 
 */
@Entity
public class OperationDuration extends Model {

	/**
	 * Time needed to perform the operation on a single unit of plantation for
	 * the most suitable terrain (percents of day). It overrides the default
	 * duration
	 */
	public int duration;

	/**
	 * Operating price for single unit of plantation
	 */
	public int operationPrice;

	/**
	 * The operation for which the duration is specified
	 */
	@ManyToOne
	public Operation operation;

	/**
	 * The base
	 */
	@ManyToOne
	public Base base;

	/**
	 * The item
	 */
	@ManyToOne
	public Item item;

}
