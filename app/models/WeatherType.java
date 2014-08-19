package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.jpa.Model;

/**
 * Weather type - sunny, cloudy, snow etc.
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="weathertype")
public class WeatherType extends Model {

	/**
	 * The name of the weather type
	 */
	public String name;
	
	@JsonIgnore
	@OneToMany(mappedBy="weatherType")
	public List<Day> days;
	
	@ManyToOne
	public SpriteImage icon;
	
	public String background_url;
	

}
