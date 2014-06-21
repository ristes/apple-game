package controllers;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import dto.FertilizerOperationDto;
import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Operation;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class FertilizationController extends Controller{
	
	/**
	 * 
	 * @param itemid, id in database for fertilization
	 * @param quantity
	 */
	public static void fertilize(Long itemid, Double quantity) {
		ItemInstance instance = ItemInstance.findById(itemid);
		if (instance==null) {
			error("Not such item.");
		}

		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in.");
		}
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
		executed.itemInstance = instance;
		executed.save();
	}

	public static Boolean checkNeedOfN(Farmer farmer) {
		Item item = Item.find("byName", "N").first();
		if (item==null) {
			error("not such item");
		}
		List<FertilizerOperationDto> all = getFertilizationOper(farmer, item);
		if (all.size()==0) {
			return false;
		}
		List<FertilizerOperationDto> executed = getExecFertOper(farmer,all.get(0).operation_id);
		double percentOfElement = FertilizerOperationDto.sumOfQuantity(executed)/FertilizerOperationDto.sumOfQuantity(all);
		if (percentOfElement<0.33) {
			return true;
		}
		return false;
	}
	
	public static Boolean checkNeedOfP(Farmer farmer) {
		Item item = Item.find("byName", "P").first();
		if (item==null) {
			error("not such item");
		}
		List<FertilizerOperationDto> all = getFertilizationOper(farmer, item);
		if (all.size()==0) {
			return false;
		}
		List<FertilizerOperationDto> executed = getExecFertOper(farmer,all.get(0).operation_id);
		double percentOfElement = FertilizerOperationDto.sumOfQuantity(executed)/FertilizerOperationDto.sumOfQuantity(all);
		if (percentOfElement<0.33) {
			return true;
		}
		return false;
	}
	
	public static Boolean checkNeedOfK(Farmer farmer) {
		Item item = Item.find("byName", "K").first();
		if (item==null) {
			error("not such item");
		}
		List<FertilizerOperationDto> all = getFertilizationOper(farmer, item);
		if (all.size()==0) {
			return false;
		}
		List<FertilizerOperationDto> executed = getExecFertOper(farmer,all.get(0).operation_id);
		double percentOfElement = FertilizerOperationDto.sumOfQuantity(executed)/FertilizerOperationDto.sumOfQuantity(all);
		if (percentOfElement<0.33) {
			return true;
		}
		return false;
	}
	
	public static List<FertilizerOperationDto> getExecFertOper(Farmer farmer,Long operation_id) {
		String sqlSelect = "select * from ExecutedOperation where field_id=:field_id and operation_id=:operation_id";
		List<FertilizerOperationDto> resultEnd = new ArrayList<FertilizerOperationDto>();
		Query query = JPA.em().createNativeQuery(sqlSelect);
		query.setParameter("field_id", farmer.field.id);
		query.setParameter("operation_id", operation_id);
		List<Object[]> result = query.getResultList();
		for (Object[] obj:result) {
			FertilizerOperationDto fo = new FertilizerOperationDto();
			fo.id = ((BigDecimal)obj[0]).longValue();
			fo.startFrom = ((Timestamp)obj[1]);
			fo.operation_id = ((BigDecimal)obj[2]).longValue();
			if (isSameYear(farmer, fo.startFrom)) {
				resultEnd.add(fo);
			}
		}
		return resultEnd;
	}
	
	public static List<FertilizerOperationDto> getFertilizationOper(Farmer farmer, Item item) {
		List<FertilizerOperationDto> fertilizationOper = new ArrayList<FertilizerOperationDto>();
		String sqlSelect = "select * from FertilizationOperation,OperationBestTimeInterval where OperationBestTimeInterval.fertilizationBestTime_id=FertilizationOperation.id and fertilizer_id=:id and date(startFrom)<=date(:date)";
		Query query = JPA.em().createNativeQuery(sqlSelect);
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		query.setParameter("id", item.id);
		query.setParameter("date", formatter.format(farmer.gameDate.date));
		List<Object[]> fertilizing = query.getResultList();
		for (Object[] o:fertilizing) {
			FertilizerOperationDto f = new FertilizerOperationDto();
			f.id = ((BigInteger)(o[0])).longValue();
			f.fertilizer_id = ((BigInteger)(o[1])).longValue();
			f.operation_id = ((BigInteger)(o[2])).longValue();
			f.startFrom = (Timestamp)(o[5]);
			f.endTo = (Timestamp)(o[4]);
			f.quantity = ((Double)(o[8])).doubleValue();
			fertilizationOper.add(f);
		}
		return fertilizationOper;
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

	
	public static Boolean isSameYear(Farmer farmer, Date date) {
		
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		if (DeseasesExpertSystem.isAfterNewYear(c.getTime())) {
			c.add(Calendar.YEAR, -1);
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, 9);
		
		if (date.after(c.getTime())) {
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH, 9);
			if (date.before(c.getTime())) {
				return true;
			}
		}
		return false;
	}
}
