package service.impl;

import exceptions.NotEnoughMoneyException;
import models.Decease;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.OccurredDecease;
import service.DateService;
import service.HarvestService;
import service.InfoTableService;
import service.InsuranceService;
import service.MoneyTransactionService;
import service.RandomGeneratorService;
import service.YieldService;
import utils.RImage;
import utils.RString;

public class InsuranceServiceImpl implements InsuranceService {

	@Override
	public Farmer buyInsurance(Farmer farmer) throws NotEnoughMoneyException {
		if (hasInsuranceThisYear(farmer) == false) {
			Double cost = calculatePriceOfInsurance(farmer);
			return commitTransaction(farmer, cost);
		}
		return farmer;

	}

	@Override
	public Farmer refundInsurance(Farmer farmer, OccurredDecease odisease){
		if (!odisease.desease.isRefundable) {
			return farmer;
		}
		Double refund = odisease.desease.getRefund(farmer);
		MoneyTransactionService moneyTrans = new TransactionServiceImpl();
		try {
			farmer = moneyTrans.commitMoneyTransaction(farmer, refund);
			InfoTableService info = new InfoTableServiceImpl();
			info.createT1(farmer, String.format(RString.get("insurrance_refund_money"),refund.intValue()),RImage.get("insurrance_refund_money"));
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		return farmer;
	}

	@Override
	public Boolean hasInsuranceThisYear(Farmer farmer) {
		Item insurrance = Item.find("byName", "insurrance").first();
		DateService dS = new DateServiceImpl();
		int year = dS.recolteYear(farmer.gameDate.date);
		ItemInstance item = ItemInstance.find("byOwnedByAndTypeAndYear", farmer,
				insurrance, year).first();
		if (item != null) {
			return true;
		}
		return false;
	}

	private Double calculatePriceOfInsurance(Farmer farmer) {
		YieldService yieldS = new YieldServiceImpl();
		RandomGeneratorService rS = new RandomGeneratorServiceImpl();
		Double rApplePrice = rS.random(20.0, 25.0);
		Double rPercent = rS.random(0.1, 0.15);
		//price in euros
		Double cost = yieldS.calculateYield(farmer) * rApplePrice * rPercent/60.0;
		return cost;
	}

	private Farmer commitTransaction(Farmer farmer, Double cost)
			throws NotEnoughMoneyException {
		MoneyTransactionService moneyTrans = new TransactionServiceImpl();
		DateService dateS = new DateServiceImpl();
		moneyTrans.commitMoneyTransaction(farmer, -Math.round(cost));
		Item insurrance = Item.find("byName", "insurrance").first();
		ItemInstance itemI = new ItemInstance();
		itemI.type = insurrance;
		itemI.ownedBy = farmer;
		itemI.quantity = 1d;
		itemI.year = dateS.recolteYear(farmer.gameDate.date);
		itemI.save();
		return farmer;
	}

}
