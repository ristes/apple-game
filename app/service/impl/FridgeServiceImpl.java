package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.Fridge;
import models.PlantType;
import models.PlantationSeedling;
import models.Yield;
import models.YieldPortion;
import service.FridgeService;
import service.ServiceInjector;
import dao.FridgeUsageDto;
import dto.FridgeShelf;
import dto.StatusDto;
import exceptions.InvalidYield;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public class FridgeServiceImpl implements FridgeService{

	@Override
	public void setFridges(Farmer farmer, 
			List<PlantationSeedling> plantationSeedlings) {
		
		Fridge noFridge = new Fridge();
		noFridge.capacity = 1000000;
		noFridge.type = FridgeService.NO_FRIDGE;
		noFridge.name = "Обично складиште";
		noFridge.farmer = farmer;
		noFridge.price = 0;
		noFridge.save();
		
		Fridge naFridge = new Fridge();
		naFridge.capacity = 0;
		naFridge.type = FridgeService.NA_FRIDGE;
		naFridge.price = 1;
		naFridge.name = "Ладилник со нормална атмосфера";
		naFridge.farmer = farmer;
		naFridge.save();
		
		Fridge caFridge = new Fridge();
		caFridge.capacity = 0;
		caFridge.type = FridgeService.CA_FRIDGE;
		caFridge.price = 2;
		caFridge.name = "Ладилник со контролирана атмосфера";
		caFridge.farmer = farmer;
		caFridge.save();
		
		
	}
	

	@Override
	public void buyFridgeCapacity(Farmer farmer, int capacity, int fridgeType) throws NotEnoughMoneyException{
		Fridge fridge = Fridge.find("byFarmerAndType", farmer,fridgeType).first();
		Double price = (double)fridge.price * capacity;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -price);
		fridge.addCapacity(capacity);
		fridge.save();
		
	}

	@Override
	public void addToFridge(Farmer farmer, Fridge fridge, PlantType type, int quantity) throws NotEnoughSpaceInFridge, InvalidYield{
		YieldPortion portion = new YieldPortion();
		Yield yield = Yield.find("byPlantation.seedling.typeAndFarmerAndYear", type, farmer, ServiceInjector.dateService.recolteYear(fridge.farmer.gameDate.date)).first();
		if (yield==null) {
			throw new InvalidYield();
		}
		if (((fridge.capacity - this.getFridgeUsage(farmer, fridge.type).used)-quantity) < 0 ) {
			throw new NotEnoughSpaceInFridge();
		}
		portion.fridge = fridge;
		portion.yield = yield;
		portion.quantity = quantity;
		portion.save();
	}

	@Override
	public void removeFromFridge(Farmer farmer, Fridge fridge, PlantType type, int quantity) throws NotEnoughApplesException{
		YieldPortion yieldPortion = YieldPortion.find("byFridge.farmerAndYield.plantation.seedling.typeAndYield.year", farmer, type, ServiceInjector.dateService.recolteYear(fridge.farmer.gameDate.date)).first();
		if (yieldPortion==null) {
			throw new NotEnoughApplesException();
		}
		if (yieldPortion.quantity < quantity) {
			throw new NotEnoughApplesException();
		}
		ServiceInjector.yieldPortionService.removeFromPortion(yieldPortion, quantity);
		yieldPortion.save();
	}
	
	public void removeAllPlantTypeFromFridge(Farmer farmer, Fridge fridge, PlantType type) {
		YieldPortion yieldPortion = YieldPortion.find("byFarmerAndPlantation.seedling.typeAndYear", farmer, type, ServiceInjector.dateService.recolteYear(fridge.farmer.gameDate.date)).first();
		yieldPortion.quantity = 0;
		yieldPortion.save();
	}

	@Override
	public FridgeUsageDto getFridgeUsage(Farmer farmer, int fridgeType) {
		FridgeUsageDto usage = new FridgeUsageDto();
		Fridge fridge = Fridge.find("byFarmerAndType",farmer, fridgeType).first();
		List<YieldPortion> portions = YieldPortion.find("byFridge.farmerAndFridge.typeAndYield.year",farmer,fridgeType,ServiceInjector.dateService.recolteYear(farmer.gameDate.date)).fetch();
		if (0 == portions.size()) {
			usage.capacity = 0;
			usage.fridgeType = fridgeType;
			usage.used = 0;
		}
		for (YieldPortion portion:portions) {
			usage.used += portion.quantity;
			FridgeShelf shelf = new FridgeShelf();
			shelf.plantType = portion.yield.plantation.seedling.type;
			shelf.quantity = portion.quantity;
			usage.shelfs.add(shelf);
		}
//		usage.capacity = fridge.capacity;
		return usage;
	
	}

	@Override
	public List<FridgeUsageDto> getFarmerFridges(Farmer farmer) {
		List<FridgeUsageDto> result = new ArrayList<FridgeUsageDto>();
		result.add(getFridgeUsage(farmer, FridgeService.NO_FRIDGE));
		result.add(getFridgeUsage(farmer, FridgeService.NA_FRIDGE));
		result.add(getFridgeUsage(farmer, FridgeService.CA_FRIDGE));
		return result;
	}


	@Override
	public Integer getTotalApplesInStock(Farmer farmer) {
		List<FridgeUsageDto> total = getFarmerFridges(farmer);
		Integer apples_in_stock = 0;
		for (FridgeUsageDto fridge:total) {
			apples_in_stock += fridge.used;
		}
		return apples_in_stock;
	}


	@Override
	public Fridge getFridge(Farmer farmer, Integer type) {
		return Fridge.find("byFarmerAndType",farmer,type).first();
	}


	@Override
	public void checkApplesState(Farmer farmer) {
		List<Yield> yields = ServiceInjector.yieldService.getPreviousYearYield(farmer);
		List<Fridge> fridges = Fridge.find("byFarmer",farmer).fetch();
		for (Fridge fridge:fridges) {
			checkApplesStateForFridge(farmer, fridge, yields);
		}
		
	}


	@Override
	public void checkApplesStateForFridge(Farmer farmer, Fridge fridge, List<Yield> yields) {
		
		for (Yield yield: yields) {
			List<YieldPortion> portions = YieldPortion.find("byFridgeAndYield", fridge, yield).fetch();
			for (YieldPortion portion: portions) {
				if (ServiceInjector.yieldPortionService.checkDeadline(farmer, fridge, portion)) {
					int quantity = portion.quantity;
					removeAllPlantTypeFromFridge(farmer, fridge, portion.yield.plantation.seedling.type);
					String message = String.format("Скапаа јаболките во %s. Изгубивте %d kg јаболки.",fridge.name, quantity);
					ServiceInjector.infoTableService.createT1(farmer, message, "/public/images.attention/ikona-za-trevoga-od-zamrznato-ovoshje.png");
				}
			}
		}
		
		
	}


	@Override
	public Boolean deadlineApples(Farmer farmer, Fridge fridge,
			YieldPortion portion) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
