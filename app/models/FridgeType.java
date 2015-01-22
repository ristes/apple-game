package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;


@Entity
@Table(name="fridge_type")
public class FridgeType extends Model{
	
	public Integer type;
	public String name;
	public Double price;

}
