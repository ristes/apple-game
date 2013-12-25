package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * The occurred decease on some plantation
 * 
 * @author ristes
 * 
 */
@Entity
public class OccurredDecease extends Model {

	/**
	 * Which decease has occurred
	 */
	@ManyToOne
	public Decease desease;

	/**
	 * The plantation where the decease occurred
	 */
	@ManyToOne
	public Plantation plantation;
}
