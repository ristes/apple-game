package dao;

import java.util.List;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import dto.ItemBoughtDto;

public interface ItemsDao {

	public List<ItemInstance> getBoughtAndUnusedItems(Farmer farmer);

	public List<ItemBoughtDto> getAllItemsBoughtAndUnunsedByFarmer(Farmer farmer);

	public List<ItemBoughtDto> getOneYearDurationItems(Farmer farmer);

	public List<ItemInstance> getItemsByStoreNameOrdered(Long id, String string);

	public Item findItemByName(String name);

}
