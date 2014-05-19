package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

/**
 * The type of the apples
 * 
 * @author ristes
 * 
 */
@Entity
public class PlantType extends Model {

	/**
	 * The allowed bases for planting of this type
	 */
	
	public String name;
	
	
	public String toString() {
		return name;
	}
}
