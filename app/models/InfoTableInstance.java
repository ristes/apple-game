package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="infotableinstance")
public class InfoTableInstance extends Model{
	
	public Boolean isRead;
	
	public String message1;
	public String message2;
	public String image2_url;
	public String image3_url;

	@ManyToOne
	public InfoTable infoTable;
	
	@ManyToOne
	public Plantation plantation;
}
