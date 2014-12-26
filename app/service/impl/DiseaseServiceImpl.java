package service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.DeceaseProtectingOperation;
import models.Disease;
import models.ExecutedOperation;
import models.Farmer;
import models.OccurredDecease;
import service.DateService;
import service.DiseaseService;
import service.ServiceInjector;
import dao.DeceasesDao;
import dao.impl.DeceasesDaoImpl;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;

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
				prob = disease.getRisk(farmer);
			} else {
				prob = disease.getRisk(farmer);
				int n = disease.getOperationsDiminushingFactor(farmer);
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
					Double demage = d.getDemage(farmer);
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

	public List<String> getOccurredDiseasesLast15Days(Farmer farmer) {
		DeceasesDao diseaseDao = new DeceasesDaoImpl();
		return diseaseDao.getOccurredDiseasesLast15Days(farmer);
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
							od.demage.intValue()), od.desease.getImageUrl());
		}
	}

}
