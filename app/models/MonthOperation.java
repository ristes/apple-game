package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="month_operation")
public class MonthOperation extends Model{
	
	public Integer month;
	
	@ManyToOne
	public Operation operation;
	
	
	public String description;
	

}
