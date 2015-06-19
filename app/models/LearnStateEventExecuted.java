package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="learn_state_event_executed")
public class LearnStateEventExecuted extends Model{
	
	@ManyToOne
	private Farmer farmer;
	
	@ManyToOne
	private LearnStateEvents event;
	
	@ManyToOne
	private Day day;

	public Farmer getFarmer() {
		return farmer;
	}

	public void setFarmer(Farmer farmer) {
		this.farmer = farmer;
	}

	public LearnStateEvents getEvent() {
		return event;
	}

	public void setEvent(LearnStateEvents event) {
		this.event = event;
	}

	public Day getDay() {
		return day;
	}

	public void setDay(Day day) {
		this.day = day;
	}
	
	

}
