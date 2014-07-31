package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

/**
 * The field on which the apples are planted. It can contains more than one type
 * of apples, represented through the {@link Plantation} class
 * 
 * @author ristes
 * 
 */
@Entity
public class Field extends Model {

	/**
	 * The price of the field (for buying)
	 */
	public int price;

	/**
	 * The units of area of the field (everywhere referred to as units of
	 * plantation)
	 */
	public double area;

	/**
	 * The owner of the field (the one that bought it)
	 */
	@OneToOne(mappedBy = "field")
	public Farmer owner;

	/**
	 * The terrain characteristics of the field
	 */
	@OneToOne
	public Terrain terrain;

	/**
	 * The plantations placed on the field
	 */
	@OneToOne
	public Plantation plantation;

	/**
	 * The operations executed on this field
	 */
	@OneToMany(mappedBy = "field")
	public List<ExecutedOperation> executedOperations;

	

	
}
