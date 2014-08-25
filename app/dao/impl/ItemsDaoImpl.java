package dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import service.DateService;
import service.impl.DateServiceImpl;
import models.Farmer;
import models.ItemInstance;
import models.ItemType;
import dao.ItemsDao;
import dto.ItemBoughtDto;

public class ItemsDaoImpl implements ItemsDao {

	@Override
	public List<ItemInstance> getBoughtAndUnusedItems(Farmer farmer) {
		String sqlSelect = "select * from iteminstance  where ownedBy_id=:farmer_id and id NOT IN (select DISTINCT(itemInstance_id) FROM executedoperation where field_id=:field_id and  not(isnull(iteminstance_id)))";
		Query query = JPA.em().createNativeQuery(sqlSelect, ItemInstance.class);
		query.setParameter("farmer_id", farmer.id);
		query.setParameter("field_id", farmer.field.id);
		List<ItemInstance> items = query.getResultList();
		return items;
	}

	@Override
	public List<ItemBoughtDto> getAllItemsBoughtDaoAndUnunsedByFarmer(
			Farmer farmer) {

		String sql = "select t1.id, t1.type_id, item.name, item.imageurl, count(t1.type_id) as count, t1.quantity, store.name as store from (select * from iteminstance where iteminstance.ownedBy_id = :farmer_id) as t1 left join item ON t1.type_id = item.id left join store ON item.store_id = store.id where item.type_id not in (:item_type_one_year) and t1.id not in (select DISTINCT (itemInstance_id) FROM executedoperation where field_id = :field_id and not (isnull(iteminstance_id))) GROUP BY t1.type_id order by t1.id";

		List<ItemBoughtDto> result = new ArrayList<ItemBoughtDto>();
		ItemType one_year_type = ItemType.find("byName", "oneyear").first();
		List<Object[]> resultSql = JPA.em().createNativeQuery(sql)
				.setParameter("farmer_id", farmer.id)
				.setParameter("field_id", farmer.field.id)
				.setParameter("item_type_one_year", one_year_type.id)
				.getResultList();
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

	@Override
	public List<ItemBoughtDto> getOneYearDurationItems(Farmer farmer) {
		List<ItemBoughtDto> result = new ArrayList<ItemBoughtDto>();
		ItemType iType = ItemType.find("byName", "oneyear").first();
		DateService dS = new DateServiceImpl();
		List<ItemInstance> items = ItemInstance.find("byType.typeAndYear",
				iType, dS.recolteYear(farmer.gameDate.date)).fetch();
		for (ItemInstance item : items) {
			ItemBoughtDto it = new ItemBoughtDto();
			it.id = item.id;
			it.type_id = item.type.id;
			it.name = item.type.name;
			it.url = item.type.imageurl;
			it.store = item.type.store.name;
			result.add(it);
		}
		return result;
	}

}
