package service.impl;

import java.util.List;
import java.util.Random;

import models.Day;
import models.Farmer;
import dao.ItemsDao;
import dao.impl.ItemsDaoImpl;
import dto.C;
import dto.ItemBoughtDto;
import service.ContextService;
import service.FarmerService;

public class FarmerServiceImpl implements FarmerService{
	
	public Double generateLuck(Farmer farmer) {
		Random random = new Random();
		Double stand_dev = farmer.luck_dev;
		Double avg = farmer.luck_avg;
		farmer.luck = (random.nextGaussian() * stand_dev + avg);
		if (farmer.luck < (avg - stand_dev)) {
			farmer.luck = avg - stand_dev;
		}
		return farmer.luck;
	}
	
	public double subtractEcoPoints(Farmer farmer, double points) {
		farmer.eco_points -= points;
		if (farmer.eco_points < 0) {
			farmer.eco_points = 0;
		}
		return farmer.eco_points;
	}
	
	public Farmer gotoNextDay(Farmer farmer) {
		ContextService ctxService = new ContextServiceImpl();
		Day today = farmer.gameDate;
		List<Day> gameDates = Day.find("byDayOrder", farmer.gameDate.dayOrder + 1)
				.fetch();
		farmer.gameDate = gameDates.get(0);
		ctxService.evaluateState(farmer);
		return farmer;
	}
	
	public Farmer gotoNextWeek(Farmer farmer) {
		int i = 0;
		while (i!=7) {
			gotoNextDay(farmer);
			i++;
		}
		return farmer;
	}

	public Farmer gotoNextMonth(Farmer farmer) {
		int i = 0;
		while (i!=31) {
			gotoNextDay(farmer);
			i++;
		}
		return farmer;
	}
	
	
	
	public Farmer buildInstance(String username, String password) {
		Farmer farmer = new Farmer();
		farmer.username = username;
		farmer.password = password;
		Day start = Day.find("dayOrder", 318l).first();
		farmer.gameDate = start;
		farmer.setBalance(120000.0);
		farmer.eco_points = 100;
		farmer.deltaCumulative = 0.0;
		farmer.cumulativeHumidity = 0.0;
		farmer.cumulativeLeafHumidity = 15d;
		farmer.luck_dev = 0.3;
		farmer.luck_avg = 0.7;
		Double luck = generateLuck(farmer);
		farmer.luck = luck;
		farmer.plant_url = "/public/images/game/plant.png";
		farmer.soil_url = C.soil_urls[0];
		farmer.coef_soil_type = 1;
		farmer.grass_growth = 5.0;
		farmer.digging_coef = 1.0;

		farmer.save();
		return farmer;
	}

	@Override
	public Farmer addCumulative(Farmer farmer, double deltaCumulative) {
		farmer.deltaCumulative += deltaCumulative;
		if (farmer.deltaCumulative > 50) {
			farmer.deltaCumulative = 50d;
		}
		return farmer;
	}

	@Override
	public Farmer setState(Farmer farmer, String state) {
		farmer.currentState = state;
		farmer.save();
		return farmer;
	}

	@Override
	public List<ItemBoughtDto> farmersItems(Farmer farmer) {
		ItemsDao itemDao = new ItemsDaoImpl();
		List<ItemBoughtDto> result = itemDao.getAllItemsBoughtDaoAndUnunsedByFarmer(farmer);
		result.addAll(itemDao.getOneYearDurationItems(farmer));
		return (result);
	}


}
