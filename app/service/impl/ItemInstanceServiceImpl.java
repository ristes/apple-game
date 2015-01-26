package service.impl;

import java.util.Calendar;
import java.util.List;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import service.ItemInstanceService;
import service.ServiceInjector;

public class ItemInstanceServiceImpl implements ItemInstanceService{

	@Override
	public Boolean has(Farmer farmer, String itemName) {
		Item item = Item.find("name=?1",itemName).first();
		List<ItemInstance> iteminstances = ItemInstance.find("type=?1 AND ownedBy=?2", item,
				farmer).fetch();
		if (iteminstances.size()==0) {
			return false;
		}
		ItemInstance iteminstance = iteminstances.get(iteminstances.size()-1);
		if (iteminstance==null) {
			return false;
		}
		int recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		int itemBought = iteminstance.year;
		if (recolteYear - itemBought >= item.expirationInYears) {
			return false;
		}
		if (iteminstance.type.expirationInMonths!=null && !iteminstance.type.expirationInMonths.equals(0)) {
			Calendar c= Calendar.getInstance();
			c.setTime(iteminstance.dateBought);
			c.add(Calendar.MONTH, iteminstance.type.expirationInMonths);
			if (c.getTime().before(farmer.gameDate.date)) {
				return false;
			}
		}
		return true;
	}

}
