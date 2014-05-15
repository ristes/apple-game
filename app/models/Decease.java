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

import dao.DeceasesDao;
import dao.DeseaseRisk;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * The deceases catalog with the occurrence conditions
 * 
 * @author ristes
 * 
 */
@Entity
public class Decease extends Model implements DeseaseRisk {

	/**
	 * The decease name
	 */
	public String name;

	/**
	 * math expr for desease occurrence
	 */
	@Column(length=65535) 
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
	 * The operations that prevent decease occurrence
	 * MAYBE THIS MAY BE REMOVED
	 */
	@ManyToMany
	public List<Operation> preventingOperations;
	
	@OneToMany(mappedBy="decease")
	public List<DeceaseProtectingOperation> protections;

	/**
	 * Which operations can heal this decease, or at least lower the losses
	 */
	@ManyToMany
	public List<Operation> healingOperation;
	

	public Double getRisk(Farmer context) {
		Double result = 0.0;
		if (expression == null || expression.equals("")) {
			return result;
		}
		Calculable value = null;
		try {
			value = new ExpressionBuilder(expression)
					.withVariable("humidity", context.gameDate.humidity)
					.withVariable("tempLow", context.gameDate.tempLow)
					.withVariable("tempHigh", context.gameDate.tempHigh)
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
		return result;
	}
	
	/**
	 * if the farmer threats properly the field with protecting operations,
	 * the probability of disease is diminishing by the coefficient
	 * obtained by this method.
	 * The default value in this case is 0.4 t.e. 40%
	 */

	public Double getOperationsDiminushing(Farmer context) {
		Double result = 0.0;
		List<ExecutedOperation> operations = context.field.executedOperations;
		List<ExecutedOperation> operationsThisYear = new ArrayList<ExecutedOperation>();
		for (ExecutedOperation operation : operations) {
			if (context.isSameYear(operation.startDate)) {
				operationsThisYear.add(operation);
			}
		}
		for (ExecutedOperation operation : operations) {
			DeceaseProtectingOperation protection = null;
			if ((protection = isOperationInProtections(operation))!=null) {
				if (context.isSameYear(operation.startDate)) {
					if (protection.isInInterval(operation.startDate)) {
						result = 0.4;
					}
				}
			}
		}
		
		return result;
		
	}
	
	private DeceaseProtectingOperation isOperationInProtections(ExecutedOperation operation) {
		for (DeceaseProtectingOperation protection : protections) {
			if (protection.operation.id == operation.operation.id) {
				return protection;
			}
		}
		return null;
	}
	
	public String toString() {
		return name;
	}

}
