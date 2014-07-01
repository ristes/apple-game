package controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Decease;
import models.ExecutedOperation;
import models.Farmer;
import models.OccurredDecease;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.Controller;
import dao.DateDao;
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
	
	public static int diseases() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("crafty/login");
		}
		String images_url = "/public/images/diseases/";
		String extension = ".png";
		List<String> result = new ArrayList<String>();
		List<DiseaseOccurenceProb> probs = getDP(farmer);
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
	//get diseases for previous 15 days
	public static void getOccurredDiseases() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		List<String> result = new ArrayList<String>();
		String sql = "select DISTINCT(name) from ((select * from occurreddecease where plantation_id=:plantation_id and date > DATE_SUB(date(:date), INTERVAL 15 DAY)) as t LEFT JOIN Decease ON t.desease_id=Decease.id)";
//		String sql = "select DISTINCT(name) from occurreddecease Decease where"
//				+ " plantation_id=:plantation_id and desease_id=Decease.id and "
//				+ "date > DATE_SUB(date(:date), INTERVAL 15 DAY)";
		
//		JPAQuery q = OccurredDecease.find("select Distinct(od.id) FROM OccurredDecease od where od.plantation.id=:plant and od.date > DATE_SUB(date(:date), INTERVAL 15 DAY)");
//		q.setParameter("plant", farmer.field.plantation);
//		q.setParameter("date", farmer.gameDate.date);
//		result = q.fetch();
		Query q = JPA.em().createNativeQuery(sql);
		q.setParameter("plantation_id", farmer.field.plantation.id);
		q.setParameter("date", farmer.gameDate.date);
		List<Object> res = q.getResultList();
		for (Object r: res) {
			String d_name = ((String)r);
			result.add(d_name);
		}
		renderJSON(result);
	}

	public static List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Decease disease) {
		
		Date curDate = DateDao.convertDateTo70(farmer.gameDate.date);
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

	
}
