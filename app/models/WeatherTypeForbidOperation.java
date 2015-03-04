package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="weather_type_operation")
public class WeatherTypeForbidOperation extends Model{

	
	@ManyToOne
	public WeatherType weatherType;
	
	@ManyToOne
	public Operation operation;
}
