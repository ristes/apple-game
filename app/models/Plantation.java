package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.ivy.core.event.download.NeedArtifactEvent;

import com.fasterxml.jackson.annotation.JsonIgnore;

import play.db.jpa.Model;
import service.RandomGeneratorService;
import service.StoreService;
import service.impl.RandomGeneratorServiceImpl;
import service.impl.StoreServiceImpl;

@Entity
@Table(name = "plantation")
public class Plantation extends Model {

	/**
	 * The hidden variable for the expected apple quantity based on the user
	 * actions
	 */
	public int currentQuantity;



	/**
	 * JSON Array representing (x,y) values of every tree positioned on the
	 * plantation in form of String (javascript parsing) ex:
	 * "[{0,0},{0,1},{1,1},{2,2}]
	 */
	@Column(length = 5000)
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
	@Deprecated
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

	@ManyToOne
	public TerrainAnalysis analyse;

	

}
