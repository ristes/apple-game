package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="fertilizationoperation")
public class FertilizationOperation extends Model {
	
	@ManyToOne
	public Item fertilizer;
	
	@ManyToOne
	public Operation operation;
	
	@OneToMany(mappedBy="fertilizationBestTime")
	public List<OperationBestTimeInterval> operationBestTimeInterval;
	
	public String toString() {
		return fertilizer.name + "-"+operation.name;
	}

}
