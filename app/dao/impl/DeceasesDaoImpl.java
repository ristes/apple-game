package dao.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import models.Base;
import models.Farmer;
import models.Field;
import models.Seedling;
import models.Disease;
import models.DeceaseImpact;
import models.ExecutedOperation;
import dao.DeceasesDao;
import dto.DiseaseProtectingOperationDto;

public class DeceasesDaoImpl implements DeceasesDao {

	public List<ExecutedOperation> executedValidOperations(Disease decease,
			Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	public DeceaseImpact getDeceaseThreshold(Disease decease,
			Seedling seadlings, Base base) {

		return DeceaseImpact.find(
				"type.id=?1 and base.id=?2 and decease.id=?3",
				seadlings.type.id, base.id, decease.id).first();
	}

	public List<String> getOccurredDiseasesLast15Days(Farmer farmer) {
		List<String> result = new ArrayList<String>();
		String sql = "select DISTINCT(name) from ((select * from occurreddecease where plantation_id=:plantation_id and date > DATE_SUB(date(:date), INTERVAL 15 DAY)) as t LEFT JOIN decease ON t.desease_id=decease.id)";
		Query q = JPA.em().createNativeQuery(sql);
		q.setParameter("plantation_id", farmer.field.plantation.id);
		q.setParameter("date", farmer.gameDate.date);
		List<Object> res = q.getResultList();
		for (Object r: res) {
			String d_name = ((String)r);
			result.add(d_name);
		}
		return result;
	}
	
	public List<DiseaseProtectingOperationDto> getDiseaseProtectingOpersShouldBeDoneToDate(Disease disease, Date curDate) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		String sqlString = "SELECT operation.id as operation_id,decease_id, deceaseProtectingFactor,startFrom, endTo FROM deceaseprotectingoperation, operation, operationbesttimeinterval where decease_id=:id and deceaseprotectingoperation.operation_id=operation.id AND operationbesttimeinterval.operation_id=deceaseprotectingoperation.id and date(:date)>=date(endTo)";
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

}
