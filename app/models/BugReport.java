package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="bug_report")
public class BugReport extends Model{
	
	private Date dateCreated;
	
	@ManyToOne
	private Farmer farmer;
	
	private String name;
	
	@Column(name="description")
	private String description;
	
	
	
	public Date getDateCreated() {
		return dateCreated;
	}



	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}



	public Farmer getFarmer() {
		return farmer;
	}



	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	@PrePersist
	public void setDate() {
		dateCreated = new Date();
	}
	
	

}
