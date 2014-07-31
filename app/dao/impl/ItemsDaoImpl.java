package dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import models.Farmer;
import models.ItemInstance;
import dao.ItemsDao;
import dto.ItemBoughtDto;

public class ItemsDaoImpl implements ItemsDao{

	@Override
	public List<ItemInstance> getBoughtAndUnusedItems(Farmer farmer) {
		String sqlSelect = "select * from ItemInstance  where ownedBy_id=:farmer_id and id NOT IN (select DISTINCT(itemInstance_id) FROM ExecutedOperation where field_id=:field_id and  not(isnull(ItemInstance_id)))";
		Query query = JPA.em().createNativeQuery(sqlSelect,
				ItemInstance.class);
		query.setParameter("farmer_id", farmer.id);
		query.setParameter("field_id", farmer.field.id);
		List<ItemInstance> items = query.getResultList();
		return items;
	}

	@Override
	public List<ItemBoughtDto> getAllItemsBoughtDaoAndUnunsedByFarmer(
			Farmer farmer) {
		String sql = "select "+
				"ItemInstance.id," +
				"ItemInstance.type_id,"+
				"Item.name,"+
				"Item.imageurl,"+
				"count(ItemInstance.type_id) as count,"+
				"ItemInstance.quantity,"+
				"Store.name as store "+
				"from "+
				"iteminstance "+
				"left join "+
				"item ON iteminstance.type_id = item.id "+
				"left join "+
				"store ON item.store_id = store.id "+
				"where "+
				"iteminstance.ownedBy_id = :farmer_id "+
				"and iteminstance.id not in (select DISTINCT "+
				"(itemInstance_id) "+
				"FROM "+
				"ExecutedOperation "+
				"where "+
				"field_id = :field_id "+
                "and not (isnull(ItemInstance_id))) "+
                "GROUP BY ItemInstance.type_id "+
                "order by ItemInstance.id";
		List<ItemBoughtDto> result = new ArrayList<ItemBoughtDto>();
		List<Object[]> resultSql = JPA.em().createNativeQuery(sql)
				.setParameter("farmer_id", farmer.id)
				.setParameter("field_id", farmer.field.id).getResultList();
		for (Object[] obj : resultSql) {
			ItemBoughtDto item = new ItemBoughtDto();
			item.id = ((BigInteger) obj[0]).longValue();
			item.type_id = ((BigInteger) obj[1]).longValue();
			item.name = (String) obj[2];
			item.url = (String) obj[3];
			item.count = ((BigInteger) obj[4]).intValue();
			if (obj[5] != null) {
				item.quantity = ((Double) obj[5]).doubleValue();
			} else {
				item.quantity = null;
			}
			item.store = (String) obj[6];
			result.add(item);

		}
		return result;
	}

}
