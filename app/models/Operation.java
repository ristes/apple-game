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
 * Definition of the operation
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="operation")
public class Operation extends Model {

	public String name;

	/**
	 * The default time needed to perform the operation on a single unit of
	 * plantation (percents of day). It can be overridden by
	 * {@link OperationDuration} for a certain types, base and terrain features.
	 */
	public int defaultDuration;

	/**
	 * Default operating price for single unit of plantation. It can be
	 * overridden by {@link OperationDuration} for a certain types, base and
	 * terrain features.
	 */
	public int defaultOperationPrice;

	/**
	 * How long will it take to finish this operation based on the field and
	 * item parameters
	 */
	@OneToMany(mappedBy = "operation")
	public List<OperationDuration> durations;
	
	@OneToMany(mappedBy="operation")
	public List<DeceaseProtectingOperation> protectedDecease;

	/**
	 * List of items capable to perform this operation
	 */
	@OneToMany(mappedBy = "operation")
	public List<Item> itemsCapableToPerform;
	
	@OneToMany(mappedBy="operation")
	public List<FertilizationOperation> fertilizationOperations;
	


	@ManyToOne
	public SpriteImage icon;

	@ManyToOne
	public SpriteImage ground;

	@ManyToOne
	public SpriteImage plant;

	@ManyToOne
	public SpriteImage effect;

	public String action;
	
	public String icon_url;

	public double rank;

	@Override
	public String toString() {
		return String.format("%d. %s(%s)", id, action, name);
	}

}
