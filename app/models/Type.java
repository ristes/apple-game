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
public class Type extends Model {

	/**
	 * The allowed bases for planting of this type
	 */
	@ManyToMany
	public List<Base> plantOn;
}
