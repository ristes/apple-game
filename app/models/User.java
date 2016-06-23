package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="user")
public class User extends Model{
	
	public String username;
	
	public String password;
	
	public String name;

	public String surname;

	public String email;

	public String picture;

}
