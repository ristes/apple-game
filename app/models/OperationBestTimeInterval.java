package models;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

@Entity
public class OperationBestTimeInterval extends Model{
	
	public Date startFrom;
	
	public Date endTo;
	
	@ManyToOne
	public DeceaseProtectingOperation operation;
	
	public Boolean isInInterval(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, startFrom.getDay());
		c.set(Calendar.MONTH, startFrom.getMonth());
		if (c.before(date)) {
			c.set(Calendar.DAY_OF_MONTH, endTo.getDay());
			c.set(Calendar.MONTH,endTo.getMonth());
			if (c.after(date)) {
				return true;
			}
		}
		return false;
	}

}
