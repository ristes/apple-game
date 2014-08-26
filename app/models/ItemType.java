package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="itemtype")
public class ItemType extends Model{

	public String name;
	
	@OneToMany(mappedBy="type")
	public List<Item> items;
}
