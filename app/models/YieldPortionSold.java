package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "yield_sold")
public class YieldPortionSold extends Model {

	public int quantity;

	public double price;

	public Date date;

	@ManyToOne
	public Yield yield;

}
