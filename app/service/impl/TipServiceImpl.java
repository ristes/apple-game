package service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import models.Farmer;
import models.Item;
import service.TipService;
import dao.ItemsDao;
import dao.impl.ItemsDaoImpl;

public class TipServiceImpl implements TipService{

	@Override
	public List<String> tipgenerator(Farmer farmer) {
		ItemsDao itemDao = new ItemsDaoImpl();
		List<Item> items = itemDao.getUnboughtItem(farmer);
		List<String> suggestToBuy = new ArrayList<String>();
		for (Item item:items) {
			if (null!=item.suggestToBuy()) {
				suggestToBuy.add(item.suggestToBuy());
			}
		}
		return suggestToBuy;
		
	}
	
	public String randomTip(List<String> tips) {
		Random rand = new Random();
		Double random = rand.nextDouble();
		if (random < 0.8) {
			return null;
		}
		if (tips.size()>0) {
			Collections.shuffle(tips);
			return tips.get(0);
		}
		return null;
		
	}

}
