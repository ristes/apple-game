package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="harvestingperiod")
public class HarvestingPeriod extends Model{
	
	public Date startFrom;
	
	public Date endTo;
	

	

}
