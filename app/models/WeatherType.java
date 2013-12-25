package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Weather type - sunny, cloudy, snow etc.
 * 
 * @author ristes
 * 
 */
@Entity
public class WeatherType extends Model {

	/**
	 * The name of the weather type
	 */
	public String name;

}
