package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

@Entity
public class TerrainSize extends Model{
	
	public String name;
	public Double size;
	public Double price;
	public String imageurl;

}
