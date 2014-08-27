package models;

import java.util.ArrayList;
import java.util.Calendar;
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

import controllers.DeseasesExpertSystem;
import dao.DeceasesDao;
import dao.DeseaseRisk;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseProtectingOperationDto;
import exceptions.PriceNotValidException;
import play.data.validation.Range;
import play.db.jpa.Model;
import service.ContextService;
import service.DateService;
import service.DiseaseService;
import service.PriceService;
import service.RandomGeneratorService;
import service.YieldService;
import service.impl.ContextServiceImpl;
import service.impl.DateServiceImpl;
import service.impl.DiseaseServiceImpl;
import service.impl.PriceServiceImpl;
import service.impl.RandomGeneratorServiceImpl;
import service.impl.YieldServiceImpl;
import utils.RImage;

/**
 * The deceases catalog with the occurrence conditions
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name="decease")
public class Decease extends Model implements DeseaseRisk {

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

	/**
	 * How much the yield will be diminished with these decease by default
	 * (percents). Zero means no loss at all; 100 means no yield at all
	 */
	@Range(min = 0, max = 100)
	public int defaultDiminishingFactor;

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

	public Double getRefund(Farmer context) {
		Double result = 0.0;
		if (expression == null || expression.equals("")) {
			return result;
		}
		Calculable value = null;
		try {
			RandomGeneratorService rgS = new RandomGeneratorServiceImpl();
			YieldService yS = new YieldServiceImpl();
			PriceService pS = new PriceServiceImpl();
			Double rand = rgS.random(0.0, 1.0);
			Double maxYield = yS.calculateYield(context);
			Double avgPrice = pS.price(context);
			value = new ExpressionBuilder(refundValue).withVariable("rand", rand).withVariable("maxYield",maxYield).withVariable("avgPrice", avgPrice).build();
			result = value.calculate();
		} catch (UnknownFunctionException ex) {
			ex.printStackTrace();
		} catch (UnparsableExpressionException ex) {
			ex.printStackTrace();
		} catch (PriceNotValidException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public Double getRisk(Farmer context) {
		Double result = 0.0;
		if (expression == null || expression.equals("")) {
			return result;
		}
		
		Calculable value = null;
		try {
			DateService dateS = new DateServiceImpl();
			RandomGeneratorService rGS = new RandomGeneratorServiceImpl();
			Double randn03015 = rGS.randomGausseGenerator(0.3, 0.15);
			value = new ExpressionBuilder(expression)
					.withVariable("humidity", context.gameDate.humidity)
					.withVariable("tempLow", context.gameDate.tempLow)
					.withVariable("tempHigh", context.gameDate.tempHigh)
					.withVariable("iceProb", context.gameDate.iceProb)
					.withVariable("heavyRain", context.gameDate.heavyRain)
					.withVariable("season_type_id",dateS.season_level(context))
					.withVariable("rain_type_id", context.gameDate.weatherType.id)
					.withVariable("randn03015", randn03015)
					.withVariable("rain_id",3)
					.withVariable("spring_id",4)
					.withVariable("humidityOfLeaf",
							context.gameDate.humidityOfLeaf).build();
		
		} catch (UnknownFunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result = value.calculate();
		DateService dS = new DateServiceImpl();
		if (result < 0.0) {
			result = 0.0;
		}
		return result;
	}

	/**
	 * if the farmer threats properly the field with protecting operations, the
	 * probability of disease is diminishing by the coefficient obtained by this
	 * method. The default value in this case is 0.4 t.e. 40%
	 */

	public int getOperationsDiminushingFactor(Farmer context) {
		int minN = 0;
		int maxN = 9;
		int minM = 0;
		int matches = 0;
		if ( context.field==null) {
			return 0;
		}
		DateService dateService = new DateServiceImpl();
		DiseaseService diseaseService = new DiseaseServiceImpl();
		List<ExecutedOperation> operations = context.field.executedOperations;
		List<ExecutedOperation> operationsThisYear = new ArrayList<ExecutedOperation>();
		for (ExecutedOperation operation : operations) {
			if (dateService.isSameYear(context,operation.startDate)) {
				operationsThisYear.add(dateService.changeYear(operation));
			}
		}
		List<DiseaseProtectingOperationDto> protections = diseaseService.getMmax(context, Decease.this);
		for (DiseaseProtectingOperationDto protection: protections) {
			if (protection.isMatched(operationsThisYear)) {
				matches++;
			}
		}
		int maxM = protections.size();
		if (maxM==0) {
			return 0;
		}
		int n = Math.round((maxN-minN)*((float)((matches-minM))/(maxM-minM)));
		
		return n;

	}

	public Double getDemage(Farmer context) {
		Double result = 0.0;
		if (!isDemageVar) {
			return result;
		}
		if (demageVarExp == null || demageVarExp.equals("")) {
			return result;
		}
		Calculable value = null;
		try {
			RandomGeneratorService rgS = new RandomGeneratorServiceImpl();
			YieldService yS = new YieldServiceImpl();
			Double rand = rgS.random(0.0, 1.0);
			Double maxYield = yS.calculateYield(context);
			value = new ExpressionBuilder(demageVarExp).withVariable("rand", rand).withVariable("maxYield", maxYield).withVariable("curYield",context.productQuantity).build();
			result = value.calculate();
		} catch (UnknownFunctionException ex) {
			ex.printStackTrace();
		} catch (UnparsableExpressionException ex) {
			ex.printStackTrace();
		} 
		return result;
	}
	
	public String getImageUrl() {
		return String.format("%s%s%s", RImage.get("disease_path"),name,".png");
	}
	

	
}
