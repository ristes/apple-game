package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * 
 * How the operations affect the probability of decease occurrence
 * 
 * @author ristes
 * 
 */
@Entity
public class DeceaseProtectingOperation extends Model {

	/**
	 * How the operation affects the probability of decease occurrence. If
	 * positive, the decease is less like to occur.It changes the threshold for
	 * decease occurrence. It is in percents (0- doesn't protect at all, 100-
	 * the decease won't occur)
	 */
	@Range(min = 0, max = 100)
	public int deceaseProtectingFactor;
	
	
	@OneToMany(mappedBy="operation")
	public List<OperationBestTimeInterval> intervalsForPrevetions; 
	
	

	/**
	 * The protecting operation
	 */
	@ManyToOne
	public Operation operation;

	/**
	 * The decease from which the operation prevents
	 */
	@ManyToOne
	public Decease decease;
	
	public Boolean isInInterval(Date date) {
		for (OperationBestTimeInterval interval : intervalsForPrevetions) {
			if (interval.isInInterval(date)) {
				return true;
			}
		}
		return false;
	}
	

}
