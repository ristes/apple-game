package dao.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Base;
import models.Farmer;
import models.Field;
import models.Seedling;
import models.Decease;
import models.DeceaseImpact;
import models.ExecutedOperation;
import dao.DeceasesDao;
import dto.DiseaseProtectingOperationDto;

public class DeceasesDaoImpl implements DeceasesDao {

	public List<ExecutedOperation> executedValidOperations(Decease decease,
			Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	public DeceaseImpact getDeceaseThreshold(Decease decease,
			Seedling seadlings, Base base) {

		return DeceaseImpact.find(
				"type.id=?1 and base.id=?2 and decease.id=?3",
				seadlings.type.id, base.id, decease.id).first();
	}

	@Override
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer) {
		List<String> result = new ArrayList<String>();
		String sql = "select DISTINCT(name) from ((select * from OccurredDecease where plantation_id=:plantation_id and date > DATE_SUB(date(:date), INTERVAL 15 DAY)) as t LEFT JOIN Decease ON t.desease_id=Decease.id)";
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
	
	public List<DiseaseProtectingOperationDto> getDiseaseProtectingOpersShouldBeDoneToDate(Decease disease, Date curDate) {
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		String sqlString = "SELECT Operation.id as operation_id,decease_id, deceaseProtectingFactor,startFrom, endTo FROM DeceaseProtectingOperation, Operation, OperationBestTimeInterval where decease_id=:id and DeceaseProtectingOperation.operation_id=Operation.id AND OperationBestTimeInterval.operation_id=DeceaseProtectingOperation.id and date(:date)>=date(endTo)";
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
