package service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.ExecutedOperation;
import models.Farmer;
import models.FertilizationOperation;
import models.Item;
import models.ItemInstance;
import models.Operation;
import models.OperationBestTimeInterval;
import service.FarmerService;
import service.FertilizeService;
import service.MoneyTransactionService;
import service.ServiceInjector;
import service.YieldService;
import dao.DaoInjector;
import dto.FertilizerOperationDto;
import exceptions.NotEnoughMoneyException;
import exceptions.NotSuchItemException;

public class FertilizeServiceImpl implements FertilizeService{

	@Override
	public Farmer fertilize(Farmer farmer, Double n, Double p, Double k, Double ca, Double b,
			Double mg, Double zn) throws NotEnoughMoneyException{
		Item N = Item.find("byName", "N").first();
		Item P = Item.find("byName", "P").first();
		Item K = Item.find("byName", "K").first();
		Item Ca = Item.find("byName", "Ca").first();
		Item B = Item.find("byName", "B").first();
		Item Mg = Item.find("byName", "Mg").first();
		Item Zn = Item.find("byName","Zn").first();

		double value = N.price * n + P.price * p + K.price * k + Ca.price * ca
				+ B.price * b + Mg.price * mg + Zn.price*mg;

		value *= farmer.field.area;

		MoneyTransactionService moneyTransactionService = new TransactionServiceImpl();
		moneyTransactionService.commitMoneyTransaction(farmer, -value);
		ServiceInjector.ecoPointsService.substract(farmer, N.pollutionCoefficient + P.pollutionCoefficient
				+ K.pollutionCoefficient + Ca.pollutionCoefficient
				+ B.pollutionCoefficient + Mg.pollutionCoefficient);
		if (n!=0.0) {
			saveItem(N, farmer, n);
		}
		if (p!=0.0) {
			saveItem(P, farmer, p);
		}
		if (k!=0.0) {
			saveItem(K, farmer, k);
		}
		if (ca!=0.0) {
			saveItem(Ca, farmer, ca);
		}
		if (b!=0.0) {
			saveItem(B, farmer, b);
		}
		if (mg!=0.0) {
			saveItem(Mg, farmer, mg);
		}
		farmer.save();
		ServiceInjector.logFarmerDataService.logExecutedOperation(farmer, (Operation)Operation.find("byName","fertilizing").first(),null);
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
		Double maxQuantity = ServiceInjector.yieldService.getMaxYieldByRecolte(farmer, ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
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
			ServiceInjector.ecoPointsService.substract(farmer,5);
		} else if (res>1.4 && res <= 2) {
			farmer.productQuantity+=maxQuantity*0.1;
			ServiceInjector.ecoPointsService.substract(farmer,10);
		} else if (res>2) {
			ServiceInjector.ecoPointsService.divide(farmer, 2.0);
		}
		return farmer;
	}
	
	public Double badgeEvaluate(Farmer farmer) {
		Double evalN = badgeEvaluateByItem(farmer, (Item)Item.find("byName","N").first());
		Double evalP = badgeEvaluateByItem(farmer, (Item)Item.find("byName","P").first());
		Double evalK = badgeEvaluateByItem(farmer, (Item)Item.find("byName","K").first());
		Double evalMg = badgeEvaluateByItem(farmer, (Item)Item.find("byName","Mg").first());
		Double evalCa = badgeEvaluateByItem(farmer, (Item)Item.find("byName","Ca").first());
		Double evalZn = badgeEvaluateByItem(farmer, (Item)Item.find("byName","Zn").first());
		
		return (evalN+evalP+evalK+evalMg+evalCa+evalZn)/6.0;
		
	}
	
	public Double badgeEvaluateByItem(Farmer farmer, Item item) {
		double res  = Math.abs(1-finalEvaluationItem(farmer, item));
		
		if (res <= BADGE_THRESHOLD) {
			return 100-res*100;
		}
		
		return 0.0;
	}
	
	public Double finalEvaluationItem(Farmer farmer, Item item) {
		List<FertilizationOperation> operations = item.fertilizationOperations;
		List<FertilizerOperationDto> execs = new ArrayList<FertilizerOperationDto>();
		for (FertilizationOperation operation: operations) {
			List<FertilizerOperationDto> exec = DaoInjector.fertilizingDao.getExecFertOper(farmer, operation.operation.id);
			execs.addAll(exec);
		}
		Double allQ = FertilizerOperationDto.sumOfQuantity(execs);
		List<FertilizerOperationDto> intervals = DaoInjector.fertilizingDao.getFertilizationOper(farmer, item);
		Double execSumQ = FertilizerOperationDto.sumOfQuantity(intervals);
		return allQ/execSumQ;
	}
	

	public Double evaluateFertilizer(Farmer farmer, Item item) {
//		FertilizingDao fDao = new FertilizingDaoImpl();
		List<FertilizationOperation> operations = item.fertilizationOperations;
		List<FertilizerOperationDto> execs = new ArrayList<FertilizerOperationDto>();
		for (FertilizationOperation operation: operations) {
			List<FertilizerOperationDto> exec = DaoInjector.fertilizingDao.getExecFertOper(farmer, operation.id);
			execs.addAll(exec);
		}
		
		List<FertilizerOperationDto> intervals = DaoInjector.fertilizingDao.getFertilizationOper(farmer, item);
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
		List<FertilizerOperationDto> all = DaoInjector.fertilizingDao.getFertilizationOper(farmer, item);
		if (all.size() == 0) {
			return false;
		}
		List<FertilizerOperationDto> executed = DaoInjector.fertilizingDao.getExecFertOper(farmer,
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

	@Override
	public List<Item> checkTheRequiredItemsFromContext(Farmer farmer) {
		List<Item> items = new ArrayList<Item>();
		if (farmer.needN) {
			items.add((Item)Item.find("byName","N").first());
		}
		if (farmer.needP) {
			items.add((Item)Item.find("byName","P").first());
		}
		if (farmer.needK) {
			items.add((Item)Item.find("byName","K").first());
		}
		if (farmer.needB) {
			items.add((Item)Item.find("byName","B").first());
		}
		if (farmer.needCa) {
			items.add((Item)Item.find("byName","Ca").first());
		}
		if (farmer.needMg) {
			items.add((Item)Item.find("byName","Mg").first());
		}
		if (farmer.needZn) {
			items.add((Item)Item.find("byName","Zn").first());
		}
		return items;
		
	}
	
	public void evalFertilizingState(Farmer farmer) {
		if ((farmer.gameDate.dayOrder % 8) == 0) {
			try {
				farmer.needN = checkNeedOfN(farmer);
				farmer.needP = checkNeedOfP(farmer);
				farmer.needK = checkNeedOfK(farmer);
				farmer.needCa = checkNeedOfCa(farmer);
				farmer.needB = checkNeedOfB(farmer);
				farmer.needMg = checkNeedOfMg(farmer);
				farmer.needZn = checkNeedOfZn(farmer);
				farmer.field.plantation.save();
			} catch (NotSuchItemException e) {
				e.printStackTrace();
			}
		}
	}
	

}
