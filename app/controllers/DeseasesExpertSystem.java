package controllers;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import models.Decease;
import models.ExecutedOperation;
import models.Farmer;
import play.db.jpa.JPA;
import play.mvc.Controller;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;

public class DeseasesExpertSystem extends Controller {
	
	public static Double coef_of_diminushing = 0.97;

	public static void getUserLuck() {
		Farmer f = AuthController.getFarmer();
		if (f != null) {
			f.generateLuck();
			Double luck = f.getLuck();
			renderJSON(luck);
		}
		renderJSON("");
	}

	public static List<DiseaseOccurenceProb> getDP(Farmer farmer) {
		
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

	public static void getDeseasePossibility() throws UnknownFunctionException,
			UnparsableExpressionException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			redirect("crafty/login");
		}
		List<DiseaseOccurenceProb> disProbs = getDP(farmer);
		renderJSON(disProbs);
	}

	public static void getOperations(Farmer farmer) {

	}

	public static List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Decease disease) {
		Boolean afterNewYear = false;
		Date date = farmer.gameDate.date;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterNewYear(date)) {
			c.set(Calendar.YEAR, 1971);
		} else {
			c.set(Calendar.YEAR, 1970);
		}
		Date curDate = c.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		String sqlString = "SELECT Operation.id as operation_id,decease_id, deceaseProtectingFactor,startFrom, endTo FROM applegame.DeceaseProtectingOperation, Operation, OperationBestTimeInterval where decease_id=:id and DeceaseProtectingOperation.operation_id=Operation.id AND OperationBestTimeInterval.operation_id=DeceaseProtectingOperation.id and date(:date)>=date(endTo)";
		Query query = JPA.em().createNativeQuery(sqlString);
		query.setParameter("id", disease.id);
		query.setParameter("date", formatter.format(curDate));
		List<DiseaseProtectingOperationDto> result = new ArrayList<DiseaseProtectingOperationDto>();
		List<Object[]> objRes = query.getResultList();
		for (Object[] obj:objRes) {
			DiseaseProtectingOperationDto prot = new DiseaseProtectingOperationDto();
			prot.operation_id = ((BigInteger)obj[0]).longValue();
			prot.decease_id = ((BigInteger)obj[1]).longValue();
			prot.deceaseProtectingFactor = ((Integer)obj[2]);
			prot.startFrom = ((Timestamp)obj[3]);
			prot.endTo = ((Timestamp)obj[4]);
			result.add(prot);
		}
		return result;
	}

	public static List<ExecutedOperation> getProtections(Farmer f) {
		List<ExecutedOperation> exOperations = f.field.executedOperations;
		return null;
	}

	public static Boolean isAfterNewYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month <= Calendar.DECEMBER && month >= Calendar.OCTOBER) {
			return false;
		}
		if (month == Calendar.SEPTEMBER) {
			if (day > 15) {
				return false;
			}
		}
		return true;
	}
}
