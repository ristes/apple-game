package service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;

import models.Badges;
import models.Day;
import models.Farmer;
import models.FarmerBadges;
import models.ItemInstance;
import play.i18n.Messages;
import service.DateService;
import service.FarmerService;
import service.ServiceInjector;
import service.StoreService;
import dao.DaoInjector;
import dto.C;
import dto.ItemBoughtDto;

public class FarmerServiceImpl implements FarmerService {
	

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


	public Farmer gotoNextDay(Farmer farmer) {
		List<Day> gameDates = Day.find("byDayOrder",
				farmer.gameDate.dayOrder + 1).fetch();
		farmer.gameDate = gameDates.get(0);
		ServiceInjector.contextService.evaluateState(farmer);
		return farmer;
	}

	public Farmer gotoNextWeek(Farmer farmer) {
		int i = 0;
		while (i != 7) {
			gotoNextDay(farmer);
			i++;
		}
		return farmer;
	}

	public Farmer gotoNextMonth(Farmer farmer) {
		int i = 0;
		while (i != 31) {
			gotoNextDay(farmer);
			i++;
		}
		return farmer;
	}

	public Farmer restartGame(Farmer farmer) {
		farmer.is_active = false;
		farmer.save();
		return farmer;
	}

	public Farmer buildFbInstance(String username, String access_token,
			String name, String surname, String email, String picture) {
		Farmer farmer = Farmer.find("username=?1 and is_active=?2", username,true).first();
		if (farmer == null) {
			farmer = buildInstance(username, "default_password");
		} 
//		else {
//			farmer = buildInstance(username, "default_password");
//		}

		farmer.fb_access_token = access_token;
		farmer.name = name;
		farmer.surname = surname;
		farmer.email = email;
		farmer.picture = picture;
		farmer.is_active = true;
		farmer.save();

		return farmer;
	}
	
	public Farmer restartGame(String username, String access_token, String name, String surname, String email, String picture) {
		Farmer farmer = buildInstance(username, "default_password");
		farmer.fb_access_token = access_token;
		farmer.name = name;
		farmer.surname = surname;
		farmer.email = email;
		farmer.picture = picture;
		farmer.is_active = true;
		farmer.save();

		return farmer;
	}

	public Farmer buildInstance(String username, String password) {
		Farmer farmer = new Farmer();
		farmer.username = username;
		farmer.password = password;
		Day start = Day.find("dayOrder", 318l).first();
		farmer.gameDate = start;
		farmer.setBalance(150000.0);
		ServiceInjector.ecoPointsService.restart(farmer);
		farmer.deltaCumulative = 0.0;
		farmer.cumulativeHumidity = 0.0;
		farmer.cumulativeLeafHumidity = 15d;
		farmer.luck_dev = 0.3;
		farmer.luck_avg = 0.7;
		farmer.apples_in_stock = 0;
		farmer.irrigation_misses = 0;
		Double luck = generateLuck(farmer);
		farmer.luck = luck;
		// farmer.plant_url = "/public/images/game/plant.png";
		farmer.soil_url = C.soil_urls[0];
		farmer.coef_soil_type = 1;
		farmer.grass_growth = 5.0;
		farmer.digging_coef = 1.0;
		farmer.is_active = true;
		farmer.season_level = 2;
		farmer.needB = false;
		farmer.needCa = false;
		farmer.needK = false;
		farmer.needMg = false;
		farmer.needN = false;
		farmer.needP = false;
		farmer.needZn = false;
		farmer.subState = SUBSTATE_TEST_PERIOD;
		farmer.save();
		ServiceInjector.storeService.buyItem(farmer, "SoilAnalyse", 1.0d, farmer.currentState);
		return farmer;
	}

	@Override
	public Farmer addCumulative(Farmer farmer, double deltaCumulative) {
		farmer.deltaCumulative += deltaCumulative;
		if (farmer.deltaCumulative > 50) {
			farmer.deltaCumulative = 50d;
		}
		farmer.save();
		return farmer;
	}

	@Override
	public Farmer setState(Farmer farmer, String state) {
		farmer.currentState = state;
		farmer.save();
		return farmer;
	}

	public Map<String,ItemBoughtDto> getCurrentItems(Farmer farmer) {
		return DaoInjector.itemsDao.getFarmerCurrentItems(farmer);
	}

	@Override
	public Map<String,ItemBoughtDto> farmersItems(Farmer farmer) {
		return getCurrentItems(farmer);
	}

	@Override
	public Farmer collectBadge(Farmer farmer, Badges badge) {
		if (badge == null) {
			return farmer;
		}
		DateService dateService = new DateServiceImpl();
		FarmerBadges farmerBadge = new FarmerBadges();
		farmerBadge.year = dateService.evaluateYearLevel(farmer.gameDate.date);
		farmerBadge.badge = badge;
		farmerBadge.farmer = farmer;
		ServiceInjector.infoTableService.createT1(farmer, badge.description,
				badge.image_url);
		farmerBadge.save();
		return farmer;
	}

	

	@Override
	public Double subtractProductQuantity(Farmer farmer, double percent,
			Boolean showInfoTable, String reason) {
		int beforeQuantity = farmer.productQuantity;
		farmer.productQuantity -= farmer.productQuantity * percent / 100.0;
		Double demage = (double)(beforeQuantity - farmer.productQuantity);
		if (demage > 5.0) {
			if (showInfoTable) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				ServiceInjector.infoTableService.createT1(farmer, Messages
						.getMessage("en", "lost_quantity_message",
								formatter.format(farmer.gameDate.date), String.valueOf(demage), reason), "");
			}
		}
		return demage;
	}

}
