package controllers;

import java.io.IOException;
import java.math.BigDecimal;
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

import dto.FertilizerOperationDto;
import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Operation;
import play.db.jpa.JPA;
import play.mvc.Controller;

public class FertilizationController extends Controller {

	public static void fertilize(Double n, Double p, Double k, Double ca,
			Double b, Double mg) throws JsonGenerationException,
			JsonMappingException, IOException {

		System.out.println(params);
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in.");
		}

		Item N = Item.find("byName", "N").first();
		Item P = Item.find("byName", "P").first();
		Item K = Item.find("byName", "K").first();
		Item Ca = Item.find("byName", "Ca").first();
		Item B = Item.find("byName", "B").first();
		Item Mg = Item.find("byName", "Mg").first();

		double value = N.price * n + P.price * p + K.price * k + Ca.price * ca
				+ B.price * b + Mg.price * mg;

		value *= farmer.field.area;
		if (farmer.balans < value) {
			JsonController.toJson("");
		}

		farmer.balans -= value;
		farmer.eco_points += N.pollutionCoefficient + P.pollutionCoefficient
				+ K.pollutionCoefficient + Ca.pollutionCoefficient
				+ B.pollutionCoefficient + Mg.pollutionCoefficient;
		saveItem(N, farmer, n);
		saveItem(P, farmer, p);
		saveItem(K, farmer, k);
		saveItem(Ca, farmer, ca);
		saveItem(B, farmer, b);
		saveItem(Mg, farmer, mg);
		farmer.save();
		JsonController.toJson(farmer);
	}

	private static void saveItem(Item item, Farmer farmer, Double quantity) {
		ItemInstance instance = new ItemInstance();
		instance.ownedBy = farmer;
		instance.type = item;
		instance.quantity = quantity;
		instance.save();
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.operation;
		executed.itemInstance = instance;
		executed.save();
	}

	/**
	 * 
	 * @param itemid
	 *            , id in database for fertilization
	 * @param quantity
	 */
	public static void fertilize1(Long itemid, Double quantity) {
		ItemInstance instance = ItemInstance.findById(itemid);
		if (instance == null) {
			error("Not such item.");
		}

		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
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
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfP(Farmer farmer) {
		Item item = Item.find("byName", "P").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfK(Farmer farmer) {
		Item item = Item.find("byName", "K").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfCa(Farmer farmer) {
		Item item = Item.find("byName", "Ca").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfB(Farmer farmer) {
		Item item = Item.find("byName", "B").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfZn(Farmer farmer) {
		Item item = Item.find("byName", "Zn").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfMg(Farmer farmer) {
		Item item = Item.find("byName", "Mg").first();
		if (item == null) {
			error("not such item");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public static Boolean checkNeedOfFertilizerType(Farmer farmer, Item item) {
		List<FertilizerOperationDto> all = getFertilizationOper(farmer, item);
		if (all.size() == 0) {
			return false;
		}
		List<FertilizerOperationDto> executed = getExecFertOper(farmer,
				all.get(0).operation_id);
		double allQuantity = FertilizerOperationDto.sumOfQuantity(all);
		double farmerQuantity = FertilizerOperationDto.sumOfQuantity(executed);
		if (allQuantity != 0.0) {
			double percentOfElement = farmerQuantity / allQuantity;
			if (percentOfElement < 0.33) {
				return true;
			}
		}
		return false;
	}

	public static List<FertilizerOperationDto> getExecFertOper(Farmer farmer,
			Long operation_id) {
		String sqlSelect = "select * from ExecutedOperation where field_id=:field_id and operation_id=:operation_id";
		List<FertilizerOperationDto> resultEnd = new ArrayList<FertilizerOperationDto>();
		Query query = JPA.em().createNativeQuery(sqlSelect);
		query.setParameter("field_id", farmer.field.id);
		query.setParameter("operation_id", operation_id);
		List<Object[]> result = query.getResultList();
		for (Object[] obj : result) {
			FertilizerOperationDto fo = new FertilizerOperationDto();
			fo.id = ((BigDecimal) obj[0]).longValue();
			fo.startFrom = ((Timestamp) obj[1]);
			fo.operation_id = ((BigDecimal) obj[2]).longValue();
			if (isSameYear(farmer, fo.startFrom)) {
				resultEnd.add(fo);
			}
		}
		return resultEnd;
	}

	public static List<FertilizerOperationDto> getFertilizationOper(
			Farmer farmer, Item item) {
		List<FertilizerOperationDto> fertilizationOper = new ArrayList<FertilizerOperationDto>();
		String sqlSelect = "select * from FertilizationOperation,OperationBestTimeInterval where OperationBestTimeInterval.fertilizationBestTime_id=FertilizationOperation.id and fertilizer_id=:id and date(startFrom)<=date(:date)";
		Query query = JPA.em().createNativeQuery(sqlSelect);
		SimpleDateFormat formatter = new SimpleDateFormat();
		formatter.applyPattern("yyyy-MM-dd");
		query.setParameter("id", item.id);
		query.setParameter("date", formatter.format(farmer.gameDate.date));
		List<Object[]> fertilizing = query.getResultList();
		for (Object[] o : fertilizing) {
			FertilizerOperationDto f = new FertilizerOperationDto();
			f.id = ((BigInteger) (o[0])).longValue();
			f.fertilizer_id = ((BigInteger) (o[1])).longValue();
			f.operation_id = ((BigInteger) (o[2])).longValue();
			f.startFrom = (Timestamp) (o[5]);
			f.endTo = (Timestamp) (o[4]);
			f.quantity = ((Double) (o[8])).doubleValue();
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
