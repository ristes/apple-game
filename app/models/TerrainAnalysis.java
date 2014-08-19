package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class TerrainAnalysis extends Model {

	public static final int ANALYSIS_PRICE = 1000;

	public String name;

	/**
	 * Price for unit area field with these features
	 */
	public Double unitPrice;
	
	@OneToMany(mappedBy="terrainAnalyse")
	public List<OperationBestTimeInterval> fertilizingBestTimeIntervals;
	
//	@OneToMany(mappedBy="terrainAnalyse")
//	public List<FertilizationOperation> fertilizationOperations;

	/**
	 * The list of features. It contains exactly one feature from each category
	 */
	@OneToMany(mappedBy = "analysis")
	public List<TerrainFeature> features;

}
