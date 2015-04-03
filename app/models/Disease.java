package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Range;
import play.db.jpa.Model;
import service.DateService;
import service.DiseaseService;
import service.PriceService;
import service.RandomGeneratorService;
import service.ServiceInjector;
import service.YieldService;
import service.impl.DateServiceImpl;
import service.impl.DiseaseServiceImpl;
import service.impl.PriceServiceImpl;
import service.impl.RandomGeneratorServiceImpl;
import service.impl.YieldServiceImpl;
import utils.RImage;
import dao.DeseaseRisk;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.C;
import dto.DiseaseProtectingOperationDto;
import exceptions.PriceNotValidException;

/**
 * The deceases catalog with the occurrence conditions
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "decease")
public class Disease extends Model{

	/**
	 * The decease name
	 */
	public String name;

	/**
	 * math expr for desease occurrence
	 */
	@Column(length = 65535)
	public String expression;

	/**
	 * The dates with maximum probability of decease occurrence
	 */
	@ElementCollection
	@CollectionTable(name = "PeakDates", joinColumns = @JoinColumn(name = "decease_id"))
	public List<Date> peakDates;

	/**
	 * The variance in days of the probability around the peak dates
	 */
	public int dayVariations;

	/**
	 * How long in days each preventing operation last i.e. how long the
	 * plantation if safe from the decease after the prevention operation is
	 * taken
	 */
	public int preventingDuration;

	/**
	 * The default threshold for decease occurring. It is overridden if
	 * {@link DeceaseImpact} is linked for the type and base
	 */
	public int defaultThreshold;

	/**
	 * if the insurrance could refund back the money
	 */
	public Boolean isRefundable;

	/**
	 * if disease has variable behavior
	 */
	public Boolean isDemageVar;

	public String refundValue;

	public String demageVarExp;

	public Boolean triggersInfoTable;

	public String infoTableText;
	
	public String description;
	
	/**
	 * type of disease 1 - disease
	 * 					2 - pest
	 * 					3 - natural disaster
	 */
	public int type;

	/**
	 * How much the yield will be diminished with these decease by default
	 * (percents). Zero means no loss at all; 100 means no yield at all
	 */
	@Range(min = 0, max = 100)
	public int defaultDiminishingFactor;

	@Column(length = 2000)
	public String hint;

	public double hintPrice;

	/**
	 * The weather preconditions for decease occurrence
	 */
	@OneToMany
	public List<WeatherPreconditions> weatherPreconditions;

	/**
	 * The threshold for decease occurrence
	 */
	@OneToMany(mappedBy = "decease")
	public List<DeceaseImpact> thresholds;

	/**
	 * The operations that prevent decease occurrence MAYBE THIS MAY BE REMOVED
	 */
	@ManyToMany
	public List<Operation> preventingOperations;

	@OneToMany(mappedBy = "decease")
	public List<DeceaseProtectingOperation> protections;

	/**
	 * Which operations can heal this decease, or at least lower the losses
	 */
	@ManyToMany
	public List<Operation> healingOperation;

	public String toString() {
		return name;
	}

	

}
