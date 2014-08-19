package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="terrainsize")
public class TerrainSize extends Model{
	
	public String name;
	public Double size;
	public Double price;
	public String imageurl;

}
