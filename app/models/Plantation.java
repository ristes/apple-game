package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

@Entity
public class Plantation extends Model {

	/**
	 * The hidden variable for the expected apple quantity based on the user
	 * actions
	 */
	public int currentQuantity;

	/**
	 * Used for visual representation of the trees
	 */
	public int treeSize;

	/**
	 * The field percentage covered with this plantation
	 */
	public int fieldPercentage;

	/**
	 * The base used for planting these trees
	 */
	@ManyToOne
	public Base base;

	/**
	 * The seedlings used for planting this plantation
	 */
	@ManyToOne
	public Seedling seadlings;

	/**
	 * Which is the field on which this plantation is planted on
	 */
	@OneToOne(mappedBy = "plantation")
	public Field field;

	/**
	 * The deceases that occurred on this plantation
	 */
	@OneToMany(mappedBy = "plantation")
	public List<OccurredDecease> deseases;

}
