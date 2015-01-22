package service.impl;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import service.ItemInstanceService;
import service.ServiceInjector;

public class ItemInstanceServiceImpl implements ItemInstanceService{

	@Override
	public Boolean has(Farmer farmer, Item item) {
		ItemInstance iteminstance = ItemInstance.find("type=?1 AND ownedBy=?2", item,
				farmer).first();
		if (iteminstance==null) {
			return false;
		}
		int recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		int itemBought = iteminstance.year;
		if (recolteYear - itemBought < item.expirationInYears) {
			return false;
		}
		return true;
	}

}
