package dao.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import service.DateService;
import service.impl.DateServiceImpl;
import models.Farmer;
import models.Item;
import dao.FertilizingDao;
import dto.FertilizerOperationDto;

public class FertilizingDaoImpl implements FertilizingDao{

	@Override
	public List<FertilizerOperationDto> getFertilizationOper(Farmer farmer,
			Item item) {
		DateService dateService = new DateServiceImpl();
		List<FertilizerOperationDto> fertilizationOper = new ArrayList<FertilizerOperationDto>();
		String sqlSelect = "select * from fertilizationoperation,operationbesttimeinterval where operationbesttimeinterval.fertilizationBestTime_id=fertilizationoperation.id and fertilizer_id=:id and date(endTo)<=date(:date) and terrainAnalyse_id=:terrain_analyse_id";
		Query query = JPA.em().createNativeQuery(sqlSelect);
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		query.setParameter("id", item.id);
		query.setParameter("date", formatter.format(dateService.convertDateTo70(farmer.gameDate.date)));
		query.setParameter("terrain_analyse_id",farmer.field.terrain.analysis.id);
		List<Object[]> fertilizing = query.getResultList();
		for (Object[] o : fertilizing) {
			FertilizerOperationDto f = new FertilizerOperationDto();
			f.id = ((BigInteger) (o[0])).longValue();
			f.fertilizer_id = ((BigInteger) (o[1])).longValue();
			f.operation_id = ((BigInteger) (o[2])).longValue();
			f.startFrom = (Timestamp) (o[5]);
			f.endTo = (Timestamp) (o[4]);
			//default value is for 1ha field, so we multiply the quantity with area size to gain the required value of the fertilizer
			f.quantity = ((Double) (o[8])).doubleValue()*farmer.field.area;
			fertilizationOper.add(f);
		}
		return fertilizationOper;
	}

	
	public List<FertilizerOperationDto> getExecFertOper(Farmer farmer,
			Long operation_id) {
		DateService dateService = new DateServiceImpl();
		String sqlSelect = "select * from executedoperation,iteminstance where field_id=:field_id and itemInstance_id=iteminstance.id and executedoperation.operation_id=:operation_id";
		List<FertilizerOperationDto> resultEnd = new ArrayList<FertilizerOperationDto>();
		Query query = JPA.em().createNativeQuery(sqlSelect);
		query.setParameter("field_id", farmer.field.id);
		query.setParameter("operation_id", operation_id);
		List<Object[]> result = query.getResultList();
		for (Object[] obj : result) {
			FertilizerOperationDto fo = new FertilizerOperationDto();
			fo.id = ((BigInteger) obj[0]).longValue();
			fo.startFrom = ((Timestamp) obj[1]);
			fo.operation_id = ((BigInteger) obj[2]).longValue();
			if (dateService.isSameYear(farmer, fo.startFrom)) {
				fo.quantity = ((Double) obj[8]).doubleValue();
				resultEnd.add(fo);
			}
			
		}
		return resultEnd;
	}
	
}
