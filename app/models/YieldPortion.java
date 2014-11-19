package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "yield_portion")
public class YieldPortion extends Model {

	public int quantity;

	@ManyToOne
	public Fridge fridge;

	@OneToMany
	public Yield yield;
}
