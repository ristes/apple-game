package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="basedisease")
public class BaseDisease extends Model{
	
	@ManyToOne
	public Base base;
	
	
	@ManyToOne
	public Disease disease;
	
	public Double prob;

}
