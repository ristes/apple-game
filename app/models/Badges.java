package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="badges")
public class Badges extends Model{

	public String name;
	
	public String description;
	
	public String image_url;
	
	public String metadata;
	
	public String akka;
	
	public Integer triggerrer;
}
