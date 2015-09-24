package service.impl;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.OccurredDecease;
import play.i18n.Messages;
import service.InsuranceService;
import service.ServiceInjector;
import exceptions.NotEnoughMoneyException;
import exceptions.PriceNotValidException;

public class InsuranceServiceImpl implements InsuranceService {

	@Override
	public ItemInstance buyInsurance(Farmer farmer) throws NotEnoughMoneyException {
		if (hasInsuranceThisYear(farmer) == false) {
			Double cost = calculatePriceOfInsurance(farmer);
			return commitTransaction(farmer, cost);
		}
		return null;

	}

	@Override
	public Farmer refundInsurance(Farmer farmer, OccurredDecease odisease){
		if (!odisease.desease.isRefundable) {
			return farmer;
		}
		Double refund = ServiceInjector.moneyConversionService.toEuros(ServiceInjector.diseaseService.getRefund(farmer,odisease.desease));
		try {
			ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, refund);
			ServiceInjector.infoTableService.createT1(farmer, Messages.getMessage("en", "insurrance_refund_money", String.valueOf(refund.intValue())),"/public/images/game/items/insurance.png");
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		return farmer;
	}
	
	public Farmer refundByQuantityLost(Farmer farmer, Integer quantity) {
		double price = 0.0;
		try {
			price = ServiceInjector.priceService.price(farmer);
		} catch (PriceNotValidException e) {
			e.printStackTrace();
		}
		Double refund = ServiceInjector.moneyConversionService.toEuros(price * quantity);
		try {
			ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, refund);
			ServiceInjector.infoTableService.createT1(farmer, Messages.getMessage("en", "insurrance_refund_money", String.valueOf(refund.intValue())),"/public/images/game/items/insurance.png");
			
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		return farmer;
	}

	@Override
	public Boolean hasInsuranceThisYear(Farmer farmer) {
		Item insurrance = Item.find("byName", "insurance").first();
		int year = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		ItemInstance item = ItemInstance.find("byOwnedByAndTypeAndYear", farmer,
				insurrance, year).first();
		if (item != null) {
			return true;
		}
		return false;
	}

	private Double calculatePriceOfInsurance(Farmer farmer) {
		Double rApplePrice = ServiceInjector.randomGeneratorService.random(20.0, 25.0);
		Double rPercent = ServiceInjector.randomGeneratorService.random(0.1, 0.15);
		
		//price in euros
		Double cost = ServiceInjector.yieldService.getMaxYieldByRecolte(farmer, ServiceInjector.dateService.recolteYear(farmer.gameDate.date)) * rApplePrice * rPercent/60.0;
		return cost;
	}

	private ItemInstance commitTransaction(Farmer farmer, Double cost)
			throws NotEnoughMoneyException {
		ServiceInjector.moneyTransactionService.commitMoneyTransaction(farmer, -Math.round(cost));
		Item insurrance = Item.find("byName", "insurrance").first();
		ItemInstance itemI = new ItemInstance();
		itemI.type = insurrance;
		itemI.ownedBy = farmer;
		itemI.quantity = 1d;
		itemI.year = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		itemI.save();
		return itemI;
	}

}
