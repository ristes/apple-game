package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * 
 * This class models the how much the operations afect in the process of healing
 * from some deceases
 * 
 * @author ristes
 * 
 */
@Entity
public class DeceaseHealingOperation extends Model {

	/**
	 * How this operation affects the healing of some deceases. It changes the
	 * decease impact through correcting it diminishing factor
	 */
	@Range(min = 0, max = 100)
	public int deceaseHealingFactor;

	/**
	 * The healing operation
	 */
	@ManyToOne
	public Operation operation;

	/**
	 * The decease from which the operation heals
	 */
	@ManyToOne
	public Disease decease;
}
