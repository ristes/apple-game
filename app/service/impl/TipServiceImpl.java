package service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import play.i18n.Messages;
import models.Farmer;
import models.Item;
import models.WeatherType;
import service.ServiceInjector;
import service.TipService;
import dao.DaoInjector;

public class TipServiceImpl implements TipService{

	@Override
	public List<String> tipgenerator(Farmer farmer) {
		List<Item> items = DaoInjector.itemsDao.getUnboughtItem(farmer);
		List<String> suggestToBuy = new ArrayList<String>();
		for (Item item:items) {
			if (null!=item.suggestToBuy()) {
				suggestToBuy.add(item.suggestToBuy());
			}
		}
		return suggestToBuy;
		
	}
	
	public String weatherForHarvestingTip(Farmer farmer) {
		if (ServiceInjector.harvestService.isInGlobalHarvetingPeriod(farmer)) {
			if (farmer.gameDate.weatherType.id == WeatherType.WEATHER_TYPE_SUNNY) {
				return Messages.get("good-weather-for-harvesting");
			}
		}
		return null;
	}
	
	public String randomTip(Farmer farmer, List<String> tips) {
		String tip = weatherForHarvestingTip(farmer);	
		if ((tip = weatherForHarvestingTip(farmer))!=null) {
			return tip;
		}
		Random rand = new Random();
		Double random = rand.nextDouble();
		if (random < 0.7) {
			return null;
		}
		if (tips.size()>0) {
			Collections.shuffle(tips);
			return tips.get(0);
		}
		return null;
		
	}

}
