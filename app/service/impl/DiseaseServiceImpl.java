package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
		List<DiseaseOccurenceProb> disProbs = new ArrayList<DiseaseOccurenceProb>();
		List<Disease> deceases = Disease.findAll();
		for (Disease disease : deceases) {
			DiseaseOccurenceProb dis = new DiseaseOccurenceProb();
			dis.name = disease.name;
			Double prob = 0.0;
			switch (disease.type) {
			case 1:
				onDisease(farmer, disease, dis);
				disProbs.add(dis);
				break;
			case DiseaseService.TYPE_PEST:
				onPest(farmer, disease, dis);
				disProbs.add(dis);
				break;
			case DiseaseService.TYPE_NATURAL_DISASTER:
				onNaturalDisaster(farmer, disease, dis);
				disProbs.add(dis);
				break;
			default:
				break;
			}

//			System.out.println(disease.name + " - " + dis.probability + " - "
//					+ farmer.luck * 100);
		}
		return disProbs;
	}

	public int diseases(Farmer farmer) {

		List<DiseaseOccurenceProb> probs = getDiseasesProb(farmer);
		for (DiseaseOccurenceProb prob : probs) {
			if (prob.probability != null) {
				if (prob.probability > (farmer.luck * 100)) {
					farmer.hasNewDisease = true;
					OccurredDecease od = new OccurredDecease();
					Disease d = Disease.find("byName", prob.name).first();
					od.desease = d;
					od.plantation = farmer.field.plantation;
					od.date = farmer.gameDate.date;
					if (d.isDemageVar) {
						Double demage = getDemage(farmer, d);
						farmer.productQuantity -= demage;
						od.demage = demage;
					} else {
						od.demage = farmer.productQuantity
								* (d.defaultDiminishingFactor / 100.0);
						farmer.productQuantity -= od.demage;
					}
					farmer.save();
					od.save();
					System.out.println(farmer.gameDate.date + "-" + d.name
							+ " farmer luck:" + farmer.luck + "<"
							+ prob.probability);
					ServiceInjector.logFarmerDataService.logOccurredDisease(
							farmer, d, od.demage.intValue());
					checkInfoTable(farmer, od);
					checkRefunding(farmer, od);
				}
			}
		}
		return 1;
	}

	public List<Disease> getOccurredDiseasesEntitiesLast15Days(Farmer farmer) {

		return DaoInjector.deceasesDao
				.getOccurredDiseasesEntitiesLast15Days(farmer);
	}

	public List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Disease disease) {
		Date curDate = ServiceInjector.dateService
				.convertDateTo70(farmer.gameDate.date);
		return DaoInjector.deceasesDao
				.getDiseaseProtectingOpersShouldBeDoneToDate(disease, curDate);
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
		if (farmer.season_level != ServiceInjector.dateService.SEASON_WINTER
				&& farmer.season_level != ServiceInjector.dateService.SEASON_AUTUMN) {
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
			Double rand = ServiceInjector.randomGeneratorService.random(0.0,
					1.0);
			Double maxYield = ServiceInjector.yieldService
					.getMaxYieldByRecolte(context, ServiceInjector.dateService
							.recolteYear(context.gameDate.date));
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

			Double randn02015 = ServiceInjector.randomGeneratorService
					.randomGausseGenerator(0.2, 0.15);
			value = new ExpressionBuilder(disease.expression)
					.withVariable("humidity", context.gameDate.humidity)
					.withVariable("tempLow", context.gameDate.tempLow)
					.withVariable("tempHigh", context.gameDate.tempHigh)
					.withVariable("iceProb", context.gameDate.iceProb)
					.withVariable("heavyRain", context.gameDate.heavyRain)
					.withVariable("season_type_id",
							ServiceInjector.dateService.season_level(context))
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

		if (result < 0.0) {
			result = 0.0;
		}
		return result;
	}

	@Override
	public Double addBaseProb(Farmer farmer, Disease disease, Double prob) {
		List<BaseDisease> baseDiseaseProbs = BaseDisease.find(
				"byBaseAndDisease", farmer.field.plantation.base, disease)
				.fetch();
		if (baseDiseaseProbs.size() == 0) {
			return prob;
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
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.NOVEMBER);
		c.set(Calendar.YEAR,
				ServiceInjector.dateService.recolteYear(context.gameDate.date));
		List<ExecutedOperation> operations = ExecutedOperation.find(
				"field=?1 and startDate>=?2", context.field, c.getTime())
				.fetch();
		List<ExecutedOperation> operationsThisYear = new ArrayList<ExecutedOperation>();
		for (ExecutedOperation operation : operations) {

			operationsThisYear.add(ServiceInjector.dateService
					.changeYear(operation));
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
			Double rand = ServiceInjector.randomGeneratorService.random(0.0,
					1.0);
			Double maxYield = ServiceInjector.yieldService
					.getMaxYieldByRecolte(farmer, ServiceInjector.dateService
							.recolteYear(farmer.gameDate.date));
			value = new ExpressionBuilder(disease.demageVarExp)
					.withVariable("rand", rand)
					.withVariable("maxYield", maxYield)
					.withVariable("curYield", farmer.productQuantity).build();
			result = value.calculate()*0.5;
		} catch (UnknownFunctionException ex) {
			ex.printStackTrace();
		} catch (UnparsableExpressionException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public String getImageUrl(Disease disease) {
		return String.format("%s%s%s", RImage.get("disease_path"),
				disease.name, ".png");
	}

	public void onDisease(Farmer farmer, Disease disease,
			DiseaseOccurenceProb dis) {
		dis.probability = getDiseaseProb(farmer, disease);

	}

	public void onPest(Farmer farmer, Disease disease, DiseaseOccurenceProb dis) {
		Double pheromoneDiminusher = 1.0;
		if (ServiceInjector.itemInstanceService.has(farmer, "pheromone")) {
			pheromoneDiminusher = 0.5;
		}
		dis.probability = getDiseaseProb(farmer, disease) * pheromoneDiminusher;

	}

	public Double getDiseaseProb(Farmer farmer, Disease disease) {
		Double prob = getRisk(farmer, disease);
		int n = getOperationsDiminushingFactor(farmer, disease);
		return prob - ((1 - Math.pow(coef_of_diminushing, n)) * 100);
	}

	public void onNaturalDisaster(Farmer farmer, Disease disease,
			DiseaseOccurenceProb dis) {
		if (disease.name.equals(DiseaseService.NAME_NATURAL_DISEASE_ICE)
				&& farmer.gameDate.weatherType.id.equals(3l)
				&& ServiceInjector.dateService.season_level(farmer) == ServiceInjector.dateService.SEASON_SUMMER) {
			if (ServiceInjector.fieldService.hasUVProtectingNet(farmer)) {
				dis.probability = 0.0;
			} else {
				dis.probability = getRisk(farmer, disease);
			}

		} else if (disease.name
				.equals(DiseaseService.NAME_NATURAL_DISEASE_FREEZING)) {
			evaluateIce(farmer);
		} else if (disease.name
				.equals(DiseaseService.NAME_NATURAL_DISEASE_UV_DEMAGE)) {
			evaluateUV(farmer);
		}

	}

	public void evaluateIce(Farmer farmer) {
		ServiceInjector.iceService.impactLowTemp(farmer);
	}

	public void evaluateUV(Farmer farmer) {
		ServiceInjector.uvService.impact(farmer);

	}

}
