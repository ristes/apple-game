package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import play.db.jpa.Model;

/**
 * The characteristics of the seedlings used for planting.
 * 
 * @author ristes
 * 
 */
@Entity
public class SeedlingType extends Model {

	

	public String description;
	
	public String type;
	
	
	@ManyToOne
	public SpriteImage image;
	
	public String toString() {
		return description;
	}

}
