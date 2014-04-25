package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import play.db.jpa.Model;

/**
 * The way the trees are planted i.e. M9, M27 etc.
 * 
 * A lot of characteristics depends on this choice
 * 
 * @author ristes
 * 
 */
@Entity
public class Base extends Model {

	/**
	 * The price to buy the corresponding base for unit of plantation
	 */
	
	public String name;
	
	public int price;

	/**
	 * The base may require some items like support handles etc.
	 */
	
	public Boolean needConstruction;
	
	public Integer minTreePerHa;
	public Integer maxTreePerHa;
	public Integer minTreeOnScreen;
	public Integer maxTreeOnScreen;
	public Integer minApplesPerHa;
	public Integer maxApplesPerHa;
	
	
	
	public String toString() {
		return name;
	}

}
