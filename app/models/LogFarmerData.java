package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="log_farmer_data")
public class LogFarmerData extends Model{
	
	public Date logdate;
	
	public Long typelog;

	@ManyToOne
	public Farmer farmer;
	
	@ManyToOne
	public Item item;
	
	@ManyToOne
	public Operation operation;
	
}
