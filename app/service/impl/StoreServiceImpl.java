package service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import controllers.JsonController;
import models.Base;
import models.Farmer;
import models.Field;
import models.Item;
import models.Plantation;
import models.Seedling;
import models.Store;
import dto.StoreDto;
import exceptions.NotEnoughMoneyException;
import service.DispensibleItemTransaction;
import service.ItemTransactionService;
import service.MoneyTransactionService;
import service.StoreService;

public class StoreServiceImpl implements StoreService {

	@Override
	public List<StoreDto> findAllStores() {
		List<StoreDto> result = new ArrayList<StoreDto>();
		List<Store> stores = Store.findAll();
		for (Store store : stores) {
			StoreDto dto = new StoreDto();
			dto.id = store.id;
			dto.name = store.name;
			dto.url = store.image.url;
			result.add(dto);
		}
		return result;
	}

	public Farmer buyItem(Farmer farmer, String itemName, Double quantity,
			String currentState){
		if (farmer != null) {
			Item item = Item.find("byName", itemName).first();
			ItemTransactionService itemTrans = new TransactionServiceImpl();
			try {
				farmer = itemTrans.commitBuyingItem(farmer, item, quantity);
			} catch (NotEnoughMoneyException ex) {
				ex.printStackTrace();
			}
			farmer.currentState = currentState;
			farmer.save();
		}
		return farmer;
	}
	
	public Farmer buyBase(Farmer farmer, Long itemid, String currentState) throws NotEnoughMoneyException{
		Field field = Field.find("owner.id", farmer.id).first();

		Plantation plantation = Plantation.buildInstance();
		plantation.base = Base.findById(itemid);

		field.plantation = plantation;

		farmer.currentState = currentState;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		moneyService.commitMoneyTransaction(farmer, -plantation.base.price);

		plantation.save();
		field.save();
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer buySeedling(Farmer farmer, Long seedlingTypeId,
			Long plantTypeId, String currentState)
			throws NotEnoughMoneyException {
		Plantation plantation = Plantation.find("field.owner.id", farmer.id)
				.first();

		plantation.seadlings = Seedling
				.find("type.id=:t and seedlingType.id=:st")
				.setParameter("t", plantTypeId)
				.setParameter("st", seedlingTypeId).first();

		farmer.currentState = currentState;
		MoneyTransactionService moneyService = new TransactionServiceImpl();
		Double value = plantation.seadlings.price * plantation.field.area;
		moneyService.commitMoneyTransaction(farmer, -value);

		plantation.save();
		farmer.save();
		return farmer;
	}

}
