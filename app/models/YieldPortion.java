package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "yield_portion")
public class YieldPortion extends Model {

	public int quantity;

	@ManyToOne
	public Fridge fridge;

	@ManyToOne
	public Yield yield;
	
	/**
	 *
	 * @param quantity
	 * @return the rest of apples that the portions does not have 
	 */
	public Integer removeFromPortion(int quantity) {
		this.quantity -= quantity;
		if (this.quantity < 0) {
			return Math.abs(this.quantity);
		}
		return 0;
	}
}
