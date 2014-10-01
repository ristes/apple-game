package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
@Table(name="spriteimage")
public class SpriteImage extends Model {

	@Required
	public String name;

	public String url;

	@Required
	public int width;

	@Required
	public int height;

	public double ratioWidth = 1;

	public double ratioHeight = 1;

	public String areaMap;

	public int frameNumber;

	public int speed;

	public boolean shouldDestroy;
	
	@OneToMany(mappedBy="image")
	public List<Store> stores;
	
	@OneToMany(mappedBy="image")
	public List<Item> items;
	
	@OneToMany(mappedBy="image")
	public List<SeedlingType> seedlings;
	
	@OneToMany(mappedBy="icon")
	public List<WeatherType> types;

	@PrePersist
	protected void onCreate() {
		url = "/public/images/game/" + name + ".png";
	}

	@PreUpdate
	protected void onUpdate() {
		url = "/public/images/game/" + name + ".png";
	}

	@Override
	public String toString() {
		return String.format("%d. %s [%dx%d] ", id, name, width, height);
	}

}
