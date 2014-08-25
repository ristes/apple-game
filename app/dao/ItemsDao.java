package dao;

import java.util.List;

import dto.ItemBoughtDto;
import models.Farmer;
import models.ItemInstance;

public interface ItemsDao {
	
	public List<ItemInstance> getBoughtAndUnusedItems(Farmer farmer);
	public List<ItemBoughtDto> getAllItemsBoughtDaoAndUnunsedByFarmer(Farmer farmer);
	public List<ItemBoughtDto> getOneYearDurationItems(Farmer farmer);

}
