package dao;

import java.util.List;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import dto.ItemBoughtDto;

public interface ItemsDao {

	public List<ItemBoughtDto> getFarmerCurrentItems(Farmer farmer);

	public List<ItemInstance> getItemsByStoreNameOrdered(Long id, String string);

	public List<Item> getUnboughtItem(Farmer farmer);

	public Item findItemByName(String name);

}
