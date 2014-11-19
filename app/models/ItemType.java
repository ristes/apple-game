package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

public class ItemType extends Model {

	public static final int INDISPANSIBLE = -1;
	public static final int ONE_YEAR = 1;
	public static final int FIVE_YEARS = 5;

}
