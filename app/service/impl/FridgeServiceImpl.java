package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.Fridge;
import models.FridgeType;
import models.PlantType;
import models.PlantationSeedling;
import models.Yield;
import models.YieldPortion;
import service.FridgeService;
import service.ServiceInjector;
import dao.FridgeUsageDto;
import dto.FridgeShelf;
import exceptions.InvalidYield;
import exceptions.NotEnoughApplesException;
import exceptions.NotEnoughMoneyException;
import exceptions.NotEnoughSpaceInFridge;

public class FridgeServiceImpl implements FridgeService {

	@Override
	public void setFridges(Farmer farmer,
			List<PlantationSeedling> plantationSeedlings) {

		Fridge noFridge = new Fridge();
		noFridge.capacity = 1000000;
		noFridge.type = FridgeType.find("type=?1", FridgeService.NO_FRIDGE)
				.first();
		noFridge.name = "Ordinary storehouses without a cooling system";
		noFridge.farmer = farmer;
		noFridge.price = 0.0;
		noFridge.activeFrom = 0;
		noFridge.save();

		Fridge naFridge = new Fridge();
		naFridge.activeFrom = 1;
		naFridge.capacity = 0;
		naFridge.type = FridgeType.find("type=?1", FridgeService.NA_FRIDGE)
				.first();
		naFridge.price = 0.3;
		naFridge.name = "Refrigerators with normal ambient";
		naFridge.farmer = farmer;
		naFridge.save();

		Fridge caFridge = new Fridge();
		caFridge.activeFrom = 2;
		caFridge.capacity = 0;
		caFridge.type = FridgeType.find("type=?1", FridgeService.CA_FRIDGE)
				.first();
		caFridge.price = 0.4;
		caFridge.name = "Refrigerators with controlled ambient";
		caFridge.farmer = farmer;
		caFridge.save();

	}

	@Override
	public void buyFridgeCapacity(Farmer farmer, int capacity,
			FridgeType fridgeType) throws NotEnoughMoneyException {
		Fridge fridge = Fridge
				.find("farmer=?1 And type=?2", farmer, fridgeType).first();
		Double price = (double) fridge.price * capacity;
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer,
				-price);
		fridge.addCapacity(capacity);
		fridge.save();

	}

	@Override
	public void addToFridge(Farmer farmer, Fridge fridge, PlantType type,
			int quantity) throws NotEnoughSpaceInFridge, InvalidYield {
		Yield yield = Yield.find(
				"plantType = ?1 AND farmer = ?2 And year=?3",
				type,
				farmer,
				ServiceInjector.dateService
						.recolteYear(fridge.farmer.gameDate.date)).first();
		if (yield == null) {
			 yield = Yield.find(
						"plantType = ?1 AND farmer = ?2 And year=?3",
						type,
						farmer,
						ServiceInjector.dateService
								.recolteYear(fridge.farmer.gameDate.date)-1).first();
			
		}
		if (yield==null) {
			throw new InvalidYield();
		}

		if (((fridge.capacity - this.getFridgeUsage(farmer, fridge.type).used) - quantity) < 0) {
			throw new NotEnoughSpaceInFridge();
		}
		YieldPortion portion = YieldPortion.find("fridge = ?1 AND yield = ?2",
				fridge, yield).first();

		if (portion == null) {
			portion = new YieldPortion();
			portion.fridge = fridge;
			portion.yield = yield;
		}
		portion.quantity += quantity;
		portion.save();
	}

	@Override
	public void removeFromFridge(Farmer farmer, Fridge fridge, PlantType type,
			int quantity) throws NotEnoughApplesException {
		YieldPortion yieldPortion = YieldPortion
				.find("fridge.farmer=?1 And yield.plantationSeedling.seedling.type=?2 And yield.year=?3 AND fridge = ?4",
						farmer,
						type,
						ServiceInjector.dateService
								.recolteYear(fridge.farmer.gameDate.date),
						
						fridge).first();
		if (yieldPortion == null) {
			
			yieldPortion = YieldPortion
					.find("fridge.farmer=?1 And yield.plantationSeedling.seedling.type=?2 And yield.year=?3 AND fridge = ?4",
							farmer,
							type,
							ServiceInjector.dateService
									.recolteYear(fridge.farmer.gameDate.date)-1,
							fridge).first();
			
			if (yieldPortion==null) {
				throw new NotEnoughApplesException();
			}
			
		}
		if (yieldPortion.quantity < quantity) {
			throw new NotEnoughApplesException();
		}
		ServiceInjector.yieldPortionService.removeFromPortion(yieldPortion,
				quantity);
		yieldPortion.save();
	}

	public void removeAllPlantTypeFromFridge(Farmer farmer, Fridge fridge,
			PlantType type) {
		YieldPortion yieldPortion = YieldPortion.find(
				"fridge=?1 AND yield.plantType=?2 And yield.year=?3",
				fridge,
				type,
				ServiceInjector.dateService
						.fridgerecolteyear(farmer.gameDate.date)).first();
		if (yieldPortion != null) {
			ServiceInjector.logFarmerDataService.logApplesBurnedInFridge(
					farmer, yieldPortion.quantity);
			yieldPortion.quantity = 0;
			yieldPortion.save();
		}
	}

	@Override
	public FridgeUsageDto getFridgeUsage(Farmer farmer, FridgeType fridgeType) {
		FridgeUsageDto usage = new FridgeUsageDto();
		Fridge fridge = Fridge.find("byFarmerAndType", farmer, fridgeType)
				.first();
		if (fridge == null) {
			return null;
		}
		List<YieldPortion> portions = YieldPortion
				.find("fridge.farmer=?1 And fridge.type=?2 And yield.year=?3 and quantity>0",
						farmer,
						fridgeType,
						ServiceInjector.dateService
								.fridgerecolteyear(farmer.gameDate.date))
				.fetch();
		if (0 == portions.size()) {
			usage.used = 0;
		}
		for (YieldPortion portion : portions) {
			usage.used += portion.quantity;
			FridgeShelf shelf = new FridgeShelf();
			shelf.plantType = portion.yield.plantationSeedling.seedling.type;
			shelf.quantity = portion.quantity;
			usage.shelfs.add(shelf);
		}
		usage.fridgeType = fridgeType.type;
		usage.capacity = fridge.capacity;
		usage.fridgeName = fridge.name;
		usage.price = fridge.type.price;
		usage.activeFrom = fridge.activeFrom;
		return usage;

	}

	@Override
	public List<FridgeUsageDto> getFarmerFridges(Farmer farmer) {
		List<FridgeUsageDto> result = new ArrayList<FridgeUsageDto>();
		FridgeUsageDto usageNo = getFridgeUsage(farmer, (FridgeType) FridgeType
				.find("type=?1", FridgeService.NO_FRIDGE).first());
		if (usageNo != null) {
			result.add(usageNo);
		}
		FridgeUsageDto usageNA = getFridgeUsage(farmer, (FridgeType) FridgeType
				.find("type=?1", FridgeService.NA_FRIDGE).first());
		if (usageNA != null) {
			result.add(usageNA);
		}
		FridgeUsageDto usageCA = getFridgeUsage(farmer, (FridgeType) FridgeType
				.find("type=?1", FridgeService.CA_FRIDGE).first());
		if (usageCA != null) {
			result.add(usageCA);
		}
		return result;
	}

	@Override
	public Integer getTotalApplesInStock(Farmer farmer) {
		List<FridgeUsageDto> total = getFarmerFridges(farmer);
		Integer apples_in_stock = 0;
		for (FridgeUsageDto fridge : total) {
			apples_in_stock += fridge.used;
		}
		return apples_in_stock;
	}

	@Override
	public Fridge getFridge(Farmer farmer, Integer type) {
		return Fridge.find("farmer=?1 And type.type=?2", farmer, type).first();
	}

	@Override
	public void checkApplesState(Farmer farmer) {
		List<Yield> yields = ServiceInjector.yieldService
				.getPreviousYearYield(farmer);
		List<Fridge> fridges = Fridge.find("byFarmer", farmer).fetch();
		for (Fridge fridge : fridges) {
			checkApplesStateForFridge(farmer, fridge, yields);
		}

	}

	@Override
	public void checkApplesStateForFridge(Farmer farmer, Fridge fridge,
			List<Yield> yields) {

		for (Yield yield : yields) {
			List<YieldPortion> portions = YieldPortion.find(
					"fridge=?1 AND yield=?2 AND quantity>0", fridge, yield)
					.fetch();
			for (YieldPortion portion : portions) {
				if (ServiceInjector.yieldPortionService.checkDeadline(farmer,
						fridge.type, portion)) {
					int quantity = portion.quantity;
					removeAllPlantTypeFromFridge(farmer, fridge,
							portion.yield.plantationSeedling.seedling.type);
					Integer lostEco = ServiceInjector.ecoPointsService.divide(farmer, 25.0);
					String message = String.format(play.i18n.Messages.get(
							"lost_quantity_in_fridge", fridge.name, quantity,lostEco));
					ServiceInjector.infoTableService.createT1(farmer, message,
							portion.yield.plantType.imageurl);
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
