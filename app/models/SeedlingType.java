package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

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
	
	public String imageurl;
	
	@OneToMany(mappedBy="type")
	public List<Yield> yields;
	
	
	@ManyToOne
	public SpriteImage image;
	
	public String toString() {
		return description;
	}

}
