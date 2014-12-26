package dao;

import java.util.List;
import java.util.Map;

import models.Farmer;
import models.Item;
import models.ItemInstance;
import dto.ItemBoughtDto;

public interface ItemsDao {

	public Map<String,ItemBoughtDto> getFarmerCurrentItems(Farmer farmer);

	public List<ItemInstance> getItemsByStoreNameOrdered(Long id, String string);

	public List<Item> getUnboughtItem(Farmer farmer);

	public Item findItemByName(String name);

}
