package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.apache.ivy.core.event.download.NeedArtifactEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.jpa.Model;

@Entity
public class Plantation extends Model {

	/**
	 * The hidden variable for the expected apple quantity based on the user
	 * actions
	 */
	public int currentQuantity;
	
	/**
	 * need of minerals
	 */
	public Boolean needN;
	public Boolean needP;
	public Boolean needK;
	public Boolean needCa;
	public Boolean needB;
	public Boolean needZn;
	public Boolean needMg;
	
	
	/**
	 * JSON Array representing (x,y) values of every tree positioned on the plantation in form of String (javascript parsing)
	 * ex: "[{0,0},{0,1},{1,1},{2,2}]
	 */
	public String treePositions;

	/**
	 * Used for visual representation of the trees
	 */
	
	public int treeSize;

	/**
	 * The field percentage covered with this plantation
	 */
	public int fieldPercentage;

	/**
	 * The base used for planting these trees
	 */
	@JsonIgnore
	@ManyToOne
	public Base base;

	/**
	 * The seedlings used for planting this plantation
	 */
	@JsonIgnore
	@ManyToOne
	public Seedling seadlings;

	/**
	 * Which is the field on which this plantation is planted on
	 */
	@JsonIgnore
	@OneToOne(mappedBy = "plantation")
	public Field field;

	/**
	 * The deceases that occurred on this plantation
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "plantation")
	public List<OccurredDecease> deseases;
	
	public static Plantation buildInstance() {
		Plantation p = new Plantation();
		p.treePositions = "[]";
		p.needB = false;
		p.needCa = false;
		p.needK = false;
		p.needMg = false;
		p.needN = false;
		p.needP = false;
		p.needZn = false;
		p.save();
		return p;
	}

}
