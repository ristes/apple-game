package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * The type of the apples
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="planttype")
public class PlantType extends Model {

	/**
	 * The allowed bases for planting of this type
	 */
	
	public String name;
	
	public String apple_color;
	
	public String imageurl;
	
	
	public String toString() {
		return name;
	}
}
