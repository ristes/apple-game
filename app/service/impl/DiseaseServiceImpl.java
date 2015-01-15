package service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.BaseDisease;
import models.DeceaseProtectingOperation;
import models.Disease;
import models.ExecutedOperation;
import models.Farmer;
import models.OccurredDecease;
import service.DateService;
import service.DiseaseService;
import service.ServiceInjector;
import utils.RImage;
import dao.DaoInjector;
import dao.DeceasesDao;
import dao.impl.DeceasesDaoImpl;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.C;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;
import exceptions.PriceNotValidException;

public class DiseaseServiceImpl implements DiseaseService {

	public Double coef_of_diminushing = 0.97;
	
	
	
	

	@Override
	public List<DiseaseOccurenceProb> getDiseasesProb(Farmer farmer) {
		DateService ds = new DateServiceImpl();
		List<DiseaseOccurenceProb> disProbs = new ArrayList<DiseaseOccurenceProb>();
		List<Disease> deceases = Disease.findAll();
		for (Disease disease : deceases) {
			DiseaseOccurenceProb dis = new DiseaseOccurenceProb();
			dis.name = disease.name;
			Double prob = 0.0;
			// ako e grad
			if (disease.id == 10l && farmer.gameDate.weatherType.id == 3l
					&& ds.season_level(farmer) == 4) {
				prob = getRisk(farmer, disease);
			} else {
				prob = getRisk(farmer, disease);
				int n = getOperationsDiminushingFactor(farmer, disease);
				dis.probability = prob
						- ((1 - Math.pow(coef_of_diminushing, n)) * 100);
				disProbs.add(dis);
			}
			System.out.println(prob + " - " + farmer.luck * 100);
		}
		return disProbs;
	}

	public int diseases(Farmer farmer) {

		List<DiseaseOccurenceProb> probs = getDiseasesProb(farmer);
		for (DiseaseOccurenceProb prob : probs) {
			if (prob.probability > (farmer.luck * 100)) {
				farmer.hasNewDisease = true;
				OccurredDecease od = new OccurredDecease();
				Disease d = Disease.find("byName", prob.name).first();
				od.desease = d;
				od.plantation = farmer.field.plantation;
				od.date = farmer.gameDate.date;
				if (d.isDemageVar) {
					Double demage = getDemage(farmer,d);
					farmer.productQuantity -= demage;
					od.demage = demage;
				} else {
					od.demage = farmer.productQuantity
							* (d.defaultDiminishingFactor / 100.0);
					farmer.productQuantity -= od.demage;
				}
				farmer.save();
				od.save();
				ServiceInjector.logFarmerDataService.logOccurredDisease(farmer, d, od.demage.intValue());
				checkInfoTable(farmer, od);
				checkRefunding(farmer, od);
			}
		}
		return 1;
	}

	public List<Disease> getOccurredDiseasesEntitiesLast15Days(Farmer farmer) {
		
		return DaoInjector.deceasesDao.getOccurredDiseasesEntitiesLast15Days(farmer);
	}

	public List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Disease disease) {
		Date curDate = ServiceInjector.dateService.convertDateTo70(farmer.gameDate.date);
		DeceasesDao diseasesDao = new DeceasesDaoImpl();
		return diseasesDao.getDiseaseProtectingOpersShouldBeDoneToDate(disease,
				curDate);
	}

	public List<ExecutedOperation> getProtections(Farmer f) {
		List<ExecutedOperation> exOperations = f.field.executedOperations;
		return exOperations;
	}

	private DeceaseProtectingOperation isOperationInProtections(
			ExecutedOperation operation, Disease disease) {
		for (DeceaseProtectingOperation protection : disease.protections) {
			if (protection.operation.id == operation.operation.id) {
				return protection;
			}
		}
		return null;
	}

	private void checkRefunding(Farmer farmer, OccurredDecease od) {
		if (od.desease.isRefundable) {
			if (ServiceInjector.insuranceService.hasInsuranceThisYear(farmer)) {
				ServiceInjector.insuranceService.refundInsurance(farmer, od);
			}
		}
	}

	private void checkInfoTable(Farmer farmer, OccurredDecease od) {
		if (od.desease.triggersInfoTable) {
			ServiceInjector.infoTableService.createT1(
					farmer,
					String.format(od.desease.infoTableText,
							od.demage.intValue()), getImageUrl(od.desease));
		}
	}

	@Override
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer) {
		return DaoInjector.deceasesDao.getOccurredDiseasesLast15Days(farmer);
	}

	@Override
	public void evaluateDiseases(Farmer farmer) {
		farmer.hasNewDisease = false;
		if (farmer.season_level !=ServiceInjector.dateService.SEASON_WINTER && farmer.season_level!=ServiceInjector.dateService.SEASON_AUTUMN) {
			if (farmer.gameDate.dayOrder % DISEASE_CHECK_PERIOD == 0) {
				ServiceInjector.diseaseService.diseases(farmer);
			}
		}
		
	}


	@Override
	public Double getRefund(Farmer context, Disease disease) {
		Double result = 0.0;
		if (disease.expression == null || disease.expression.equals("")) {
			return result;
		}
		Calculable value = null;
		try {
			Double rand = ServiceInjector.randomGeneratorService.random(0.0, 1.0);
			Double maxYield = ServiceInjector.yieldService.getMaxYieldByRecolte(context, ServiceInjector.dateService.recolteYear(context.gameDate.date));
			Double avgPrice = ServiceInjector.priceService.price(context);
			value = new ExpressionBuilder(disease.refundValue)
					.withVariable("rand", rand)
					.withVariable("maxYield", maxYield)
					.withVariable("avgPrice", avgPrice).build();
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

	@Override
	public Double getRisk(Farmer context, Disease disease) {
		Double result = 0.0;
		if (disease.expression == null || disease.expression.equals("")) {
			return result;
		}

		Calculable value = null;
		try {
			
			Double randn02015 = ServiceInjector.randomGeneratorService.randomGausseGenerator(0.2, 0.15);
			value = new ExpressionBuilder(disease.expression)
					.withVariable("humidity", context.gameDate.humidity)
					.withVariable("tempLow", context.gameDate.tempLow)
					.withVariable("tempHigh", context.gameDate.tempHigh)
					.withVariable("iceProb", context.gameDate.iceProb)
					.withVariable("heavyRain", context.gameDate.heavyRain)
					.withVariable("season_type_id", ServiceInjector.dateService.season_level(context))
					.withVariable("rain_type_id",
							context.gameDate.weatherType.id)
					.withVariable("randn02015", randn02015)
					.withVariable("rain_id", 3)
					.withVariable("summer_id", 4)
					.withVariable("humidityOfLeaf",
							context.gameDate.humidityOfLeaf).build();
			
		} catch (UnknownFunctionException e) {
			e.printStackTrace();
		} catch (UnparsableExpressionException e) {
			e.printStackTrace();
		}
		result = value.calculate();
		result = addBaseProb(context, disease, result);
		if (disease.name.equals("IceDemage")) {
			if (ServiceInjector.dateService.season_level(context)!=C.SEASON_SUMMER || ServiceInjector.fieldService.hasUVProtectingNet(context)) {
				result=0.0;			}
		}
		if (result < 0.0) {
			result = 0.0;
		}
		return result;
	}
	
	@Override
	public Double addBaseProb(Farmer farmer, Disease disease, Double prob) {
		List<BaseDisease> baseDiseaseProbs = BaseDisease.find("byBaseAndDisease", farmer.field.plantation.base, disease).fetch();
		if (baseDiseaseProbs.size()==0) {
			return 1.0;
		}
		return prob * baseDiseaseProbs.get(0).prob;
	}

	/**
	 * if the farmer threats properly the field with protecting operations, the
	 * probability of disease is diminishing by the coefficient obtained by this
	 * method. The default value in this case is 0.4 t.e. 40%
	 */

	@Override
	public int getOperationsDiminushingFactor(Farmer context, Disease disease) {
		int minN = 0;
		int maxN = 9;
		int minM = 0;
		int matches = 0;
		if (context.field == null) {
			return 0;
		}
		List<ExecutedOperation> operations = context.field.executedOperations;
		List<ExecutedOperation> operationsThisYear = new ArrayList<ExecutedOperation>();
		for (ExecutedOperation operation : operations) {
			if (ServiceInjector.dateService.isSameYear(context, operation.startDate)) {
				operationsThisYear.add(ServiceInjector.dateService.changeYear(operation));
			}
		}
		List<DiseaseProtectingOperationDto> protections = ServiceInjector.diseaseService
				.getMmax(context, disease);
		for (DiseaseProtectingOperationDto protection : protections) {
			if (protection.isMatched(operationsThisYear)) {
				matches++;
			}
		}
		int maxM = protections.size();
		if (maxM == 0) {
			return 0;
		}
		int n = Math.round((maxN - minN)
				* ((float) ((matches - minM)) / (maxM - minM)));

		return n;

	}

	@Override
	public Double getDemage(Farmer farmer, Disease disease) {
		Double result = 0.0;
		if (!disease.isDemageVar) {
			return result;
		}
		if (disease.demageVarExp == null || disease.demageVarExp.equals("")) {
			return result;
		}
		Calculable value = null;
		try {
			Double rand = ServiceInjector.randomGeneratorService.random(0.0, 1.0);
			Double maxYield = ServiceInjector.yieldService.getMaxYieldByRecolte(farmer, ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
			value = new ExpressionBuilder(disease.demageVarExp)
					.withVariable("rand", rand)
					.withVariable("maxYield", maxYield)
					.withVariable("curYield", farmer.productQuantity).build();
			result = value.calculate();
		} catch (UnknownFunctionException ex) {
			ex.printStackTrace();
		} catch (UnparsableExpressionException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public String getImageUrl(Disease disease) {
		return String
				.format("%s%s%s", RImage.get("disease_path"), disease.name, ".png");
	}
	

}
