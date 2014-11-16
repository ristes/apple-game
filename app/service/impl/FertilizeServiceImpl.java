package service.impl;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import service.DateService;
import service.FarmerService;
import service.FertilizeService;
import service.MoneyTransactionService;
import service.YieldService;
import service.impl.DateServiceImpl;
import service.impl.FarmerServiceImpl;
import models.ExecutedOperation;
import models.Farmer;
import models.FertilizationOperation;
import models.Item;
import models.ItemInstance;
import models.OperationBestTimeInterval;
import controllers.AuthController;
import controllers.JsonController;
import controllers.YieldController;
import dao.FertilizingDao;
import dao.impl.FertilizingDaoImpl;
import dto.FertilizerOperationDto;
import exceptions.NotEnoughMoneyException;
import exceptions.NotSuchItemException;

public class FertilizeServiceImpl implements FertilizeService{

	@Override
	public Farmer fertilize(Farmer farmer, Double n, Double p, Double k, Double ca, Double b,
			Double mg) throws NotEnoughMoneyException{
		Item N = Item.find("byName", "N").first();
		Item P = Item.find("byName", "P").first();
		Item K = Item.find("byName", "K").first();
		Item Ca = Item.find("byName", "Ca").first();
		Item B = Item.find("byName", "B").first();
		Item Mg = Item.find("byName", "Mg").first();

		double value = N.price * n + P.price * p + K.price * k + Ca.price * ca
				+ B.price * b + Mg.price * mg;

		value *= farmer.field.area;

		MoneyTransactionService moneyTransactionService = new TransactionServiceImpl();
		moneyTransactionService.commitMoneyTransaction(farmer, -value);
		farmer.eco_points -= N.pollutionCoefficient + P.pollutionCoefficient
				+ K.pollutionCoefficient + Ca.pollutionCoefficient
				+ B.pollutionCoefficient + Mg.pollutionCoefficient;
		saveItem(N, farmer, n);
		saveItem(P, farmer, p);
		saveItem(K, farmer, k);
		saveItem(Ca, farmer, ca);
		saveItem(B, farmer, b);
		saveItem(Mg, farmer, mg);
		farmer.save();
		return farmer;
		
	}
	
	private void saveItem(Item item, Farmer farmer, Double quantity) {
		ItemInstance instance = new ItemInstance();
		instance.ownedBy = farmer;
		instance.type = item;
		instance.quantity = quantity;
		instance.save();
		ExecutedOperation executed = new ExecutedOperation();
		executed.field = farmer.field;
		executed.startDate = farmer.gameDate.date;
		executed.operation = instance.type.fertilizationOperations.get(0).operation;
		executed.itemInstance = instance;
		executed.save();
	}
	
	public int finalEvaluationFertilizer(Farmer farmer) {
		
		farmer = finalEvaluation("N",farmer);
		farmer = finalEvaluation("P",farmer);
		farmer = finalEvaluation("K",farmer);
		farmer = finalEvaluation("Zn", farmer);
		farmer = finalEvaluation("Ca",farmer);
		farmer = finalEvaluation("Mg", farmer);
		farmer.save();
		return 1;
	}

	public Farmer finalEvaluation(String name, Farmer farmer) {
		Item item = Item.find("byName",name).first();
		if (item==null){
			return farmer;
		}
		FarmerService farmerService = new FarmerServiceImpl();
		
		YieldService yieldService = new YieldServiceImpl();
		Double maxQuantity = yieldService.calculateYield(farmer);
		Double res = finalEvaluationItem(farmer,item);
		Double var = Math.abs(1.0-res);
		if (var<=0.1) {
			farmer.productQuantity+=maxQuantity*0.33;
		} else if (res<0.9 && res >= 0.7) {
			farmer.productQuantity+=maxQuantity*0.2;
		} else if (res<0.7 && res >= 0.4) {
			farmer.productQuantity+=maxQuantity*0.1;
		} else if (res<0.4 && res >= 0.2) {
			farmer.productQuantity+=maxQuantity*0.05;
		} else if (res>1.1 && res <= 1.4) {
			farmer.productQuantity+=maxQuantity*0.2;
			farmerService.subtractEcoPoints(farmer,5);
		} else if (res>1.4 && res <= 2) {
			farmer.productQuantity+=maxQuantity*0.1;
			farmerService.subtractEcoPoints(farmer,10);
		} else if (res>2) {
			//farmer.productQuantity+=maxQuantity*0.05;
			farmer.eco_points /= 2;
		}
		return farmer;
	}
	
	public Double finalEvaluationItem(Farmer farmer, Item item) {
		FertilizingDao fDao = new FertilizingDaoImpl();
		List<FertilizationOperation> operations = item.fertilizationOperations;
		List<FertilizerOperationDto> execs = new ArrayList<FertilizerOperationDto>();
		for (FertilizationOperation operation: operations) {
			List<FertilizerOperationDto> exec = fDao.getExecFertOper(farmer, operation.operation.id);
			execs.addAll(exec);
		}
		Double allQ = FertilizerOperationDto.sumOfQuantity(execs);
		List<FertilizerOperationDto> intervals = fDao.getFertilizationOper(farmer, item);
		Double execSumQ = FertilizerOperationDto.sumOfQuantity(intervals);
		return execSumQ/allQ;
	}
	

	public Double evaluateFertilizer(Farmer farmer, Item item) {
		FertilizingDao fDao = new FertilizingDaoImpl();
		List<FertilizationOperation> operations = item.fertilizationOperations;
		List<FertilizerOperationDto> execs = new ArrayList<FertilizerOperationDto>();
		for (FertilizationOperation operation: operations) {
			List<FertilizerOperationDto> exec = fDao.getExecFertOper(farmer, operation.id);
			execs.addAll(exec);
		}
		
		List<FertilizerOperationDto> intervals = fDao.getFertilizationOper(farmer, item);
		int correct = 0;
		for (FertilizerOperationDto interval: intervals) {
			for (FertilizerOperationDto exec: execs) {
				if (exec.isInInterval(interval)) {
					double ratio = exec.quantity / interval.quantity;
					if (ratio > 0.33) {
						correct++;
					}
				}
			}
		}
		return correct/(double)intervals.size();
	}
	
	
	
	public List<OperationBestTimeInterval> intervalsForFertilizer(Item item) {
		List<OperationBestTimeInterval> result = new ArrayList<OperationBestTimeInterval>();
		List<FertilizationOperation> operations = item.operation.fertilizationOperations;
		for (FertilizationOperation operation: operations) {
			result.addAll(operation.operationBestTimeInterval);
		}
		return result;
	}

	
	
	public Boolean isInInterval(List<OperationBestTimeInterval> intervalsForPrevetions,  Date date) {
		for (OperationBestTimeInterval interval : intervalsForPrevetions) {
			if (interval.isInInterval(date)) {
				return true;
			}
		}
		return false;
	}
	
	
	

	
	public Boolean checkNeedOfN(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "N").first();
		if (item == null) {
			throw new NotSuchItemException("N");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfP(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "P").first();
		if (item == null) {
			throw new NotSuchItemException("P");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfK(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "K").first();
		if (item == null) {
			throw new NotSuchItemException("K");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfCa(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "Ca").first();
		if (item == null) {
			throw new NotSuchItemException("Ca");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfB(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "B").first();
		if (item == null) {
			throw new NotSuchItemException("B");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfZn(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "Zn").first();
		if (item == null) {
			throw new NotSuchItemException("Zn");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfMg(Farmer farmer) throws NotSuchItemException{
		Item item = Item.find("byName", "Mg").first();
		if (item == null) {
			throw new NotSuchItemException("Mg");
		}
		return checkNeedOfFertilizerType(farmer, item);
	}

	public Boolean checkNeedOfFertilizerType(Farmer farmer, Item item) {
		FertilizingDao fDao = new FertilizingDaoImpl();
		List<FertilizerOperationDto> all = fDao.getFertilizationOper(farmer, item);
		if (all.size() == 0) {
			return false;
		}
		List<FertilizerOperationDto> executed = fDao.getExecFertOper(farmer,
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
	

}
