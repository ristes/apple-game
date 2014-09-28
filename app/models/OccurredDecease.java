package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * The occurred decease on some plantation
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="occurreddecease")
public class OccurredDecease extends Model {

	/**
	 * Which decease has occurred
	 */
	@ManyToOne
	public Disease desease;

	/**
	 * The plantation where the decease occurred
	 */
	@ManyToOne
	public Plantation plantation;
	
	public Date date;
	
	public Double demage;
}
