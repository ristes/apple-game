package dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Query;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.ItemType;
import play.db.jpa.JPA;
import service.DateService;
import service.ServiceInjector;
import service.impl.DateServiceImpl;
import dao.ItemsDao;
import dto.ItemBoughtDto;

public class ItemsDaoImpl implements ItemsDao {

	@Override
	public Map<String, ItemBoughtDto> getFarmerCurrentItems(Farmer farmer) {

		List<ItemInstance> farmerItems = ItemInstance.find("ownedBy.id=?1 and type.visibleInBoughtItems=?2",
				farmer.id,true).fetch();
		// List<ItemBoughtDto> result = new ArrayList<ItemBoughtDto>();
		Map<String, ItemBoughtDto> boughtItems = new HashMap<String, ItemBoughtDto>();
		for (ItemInstance item : farmerItems) {
			// item.type.expirationInYears
			int recolteYear = ServiceInjector.dateService
					.recolteYear(farmer.gameDate.date);
			if (item != null && item.type != null && item.year != null) {
				if (item.type.expirationInYears == 0
						|| ((item.year + item.type.expirationInYears-1) == recolteYear)) {
					if (boughtItems.containsKey(item.type.name)) {
						boughtItems.get(item.type.name).count++;
					} else {
						if (item.type.expirationInMonths!=null && !item.type.expirationInMonths.equals(0)) {
							Calendar c = Calendar.getInstance();
							c.setTime(item.dateBought);
							c.add(Calendar.MONTH, item.type.expirationInMonths);
							if (c.getTime().before(farmer.gameDate.date)) {
								continue;
							}
						}
						ItemBoughtDto dto = new ItemBoughtDto(item);
						dto.count = 1;
						boughtItems.put(item.type.name, dto);
					}
				}
			}
		}

		return boughtItems;
	}

	@Override
	public List<ItemInstance> getItemsByStoreNameOrdered(Long farmerId,
			String string) {
		return ItemInstance.find(
				"ownedBy.id = ?1 and type.store.name=?2 order by id desc",
				farmerId, "irrigation").fetch();
	}

	@Override
	public Item findItemByName(String name) {
		List<Item> res = Item.find("name", name).fetch();
		if (res == null || res.isEmpty()) {
			return null;
		} else {
			return res.get(0);
		}
	}

	@Override
	public List<Item> getUnboughtItem(Farmer farmer) {

		Map<String, ItemBoughtDto> result = getFarmerCurrentItems(farmer);
		List<ItemBoughtDto> boughtItems = new ArrayList<ItemBoughtDto>();
		for (Entry<String, ItemBoughtDto> entry : result.entrySet()) {
			boughtItems.add(entry.getValue());
		}
		List<Item> all = Item.find("byStore.name","other").fetch();
		List<Item> results = new ArrayList<Item>();
		

		for (Item item : all) {
			if (!isIn(item, boughtItems)) {
				if (item.visibleInBoughtItems) {
				results.add(item);
				}
			}
		}
		return results;

	}

	private Boolean isIn(Item item, List<ItemBoughtDto> result) {
		for (ItemBoughtDto i : result) {
			if (item.id.equals(i.item_id)) {
				return true;
			}
		}
		return false;
	}

}
