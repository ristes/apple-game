package service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import controllers.AuthController;
import controllers.DeseasesExpertSystem;
import models.Decease;
import models.DeceaseProtectingOperation;
import models.ExecutedOperation;
import models.Farmer;
import models.OccurredDecease;
import dao.DeceasesDao;
import dao.impl.DeceasesDaoImpl;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;
import service.ContextService;
import service.DateService;
import service.DiseaseService;

public class DiseaseServiceImpl implements DiseaseService{
	
	public Double coef_of_diminushing = 0.97;

	@Override
	public List<DiseaseOccurenceProb> getDiseasesProb(Farmer farmer) {
		List<DiseaseOccurenceProb> disProbs = new ArrayList<DiseaseOccurenceProb>();
		List<Decease> deceases = Decease.findAll();
		for (Decease disease : deceases) {
			DiseaseOccurenceProb dis = new DiseaseOccurenceProb();
			dis.name = disease.name;
			Double prob = disease.getRisk(farmer);
			int n = disease.getOperationsDiminushingFactor(farmer);
			dis.probability = prob - ((1- Math.pow(coef_of_diminushing, n))*100);
			disProbs.add(dis);
		}
		return disProbs;
	}
	
	public int diseases(Farmer farmer) {
		
		String images_url = "/public/images/diseases/";
		String extension = ".png";
		List<String> result = new ArrayList<String>();
		List<DiseaseOccurenceProb> probs = getDiseasesProb(farmer);
		for (DiseaseOccurenceProb prob: probs) {
			if (prob.probability>(farmer.luck*100)) {
				OccurredDecease od = new OccurredDecease();
				Decease d = Decease.find("byName", prob.name).first();
				od.desease = d;
				od.plantation = farmer.field.plantation;
				od.date = farmer.gameDate.date;
				od.save();
				farmer.productQuantity -= farmer.productQuantity*(d.defaultDiminishingFactor/100.0);
				farmer.save();
			}
		}
		return 1;
	}
	
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer) {
		DeceasesDao diseaseDao = new DeceasesDaoImpl();
		return diseaseDao.getOccurredDiseasesLast15Days(farmer);
	}
	
	public List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Decease disease) {
		DateService dateService = new DateServiceImpl();
		Date curDate = dateService.convertDateTo70(farmer.gameDate.date);
		DeceasesDao diseasesDao = new DeceasesDaoImpl();
		return diseasesDao.getDiseaseProtectingOpersShouldBeDoneToDate(disease, curDate);
	}

	public List<ExecutedOperation> getProtections(Farmer f) {
		List<ExecutedOperation> exOperations  = f.field.executedOperations;
		return exOperations;
	}
	
	

	private DeceaseProtectingOperation isOperationInProtections(
			ExecutedOperation operation, Decease disease) {
		for (DeceaseProtectingOperation protection : disease.protections) {
			if (protection.operation.id == operation.operation.id) {
				return protection;
			}
		}
		return null;
	}

	


}
