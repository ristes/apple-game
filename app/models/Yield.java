package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.jpa.Model;

/**
 * The maximum apple yield categorization of each combination of base, type and
 * terrain
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "yield")
public class Yield extends Model {

	/**
	 * The maximum expected quantity, obtained with perfect care and conditions
	 */
	public int quantity;

	@ManyToOne
	public Farmer farmer;

	public Integer year;

	@ManyToOne
	@JoinColumn(name = "plantation_id")
	public PlantationSeedling plantationSeedling;

	/**
	 * The apple type
	 */
	
	@ManyToOne
	public PlantType plantType;

	@Transient
	public int storedQuantity;

	@OneToMany(mappedBy = "yield")
	public List<YieldPortion> yieldPortions;

	@OneToMany(mappedBy = "yield")
	public List<YieldPortionSold> yieldPortionsSold;

}
