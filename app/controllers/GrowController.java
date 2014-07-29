package controllers;

import java.util.Calendar;
import java.util.List;

import dao.DateDao;
import dao.SeedlingDao;
import dto.C;
import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.ItemInstance;
import models.Yield;
import play.mvc.Controller;

public class GrowController extends Controller {

	public final static String EXTENSION_BIG_APPLES_GREEN = "abgr";
	public final static String EXTENSION_BIG_APPLES_RED = "abr";
	public final static String EXTENSION_BIG_APPLES_GOLD = "abg";
	public final static String EXTENSION_SMALL_APPLES = "as";

	public final static String EXTENSION_WHITE_TREE = "b";

	public static String evaluatePlantImage(Farmer farmer) {
		String image_path = "/public/images/game/apple_tree/";
		StringBuilder additional = new StringBuilder();
		int season = WeatherController.season_level(farmer);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(farmer.gameDate.date);
		int year_level = 1;
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		year_level = year_tree_image(WeatherController.evaluateYearLevel(farmer.gameDate.date));

		additional.append(checkToPutApplesOnTree(farmer, year, year_level, month,
				season));
		additional.append(checkWhiteSprayed(farmer, year_level));
		return image_path + String.valueOf(year_level) + String.valueOf(season)
				+ additional + ".png";
	}

	public static int year_tree_image(int year_level) {
		if (year_level>=3) {
			return 3;
		}
		return year_level;
	}
	
	public static String checkToPutApplesOnTree(Farmer farmer, int year,
			int year_level, int month, int season) {
		String result = "";
		if (year_level == 3) {
			if (month == Calendar.AUGUST) {
				result = EXTENSION_SMALL_APPLES;
			} else if (month == Calendar.SEPTEMBER) {
				// has been harvested this year to show apples on plant
				if (Yield.find("byFarmerAndYear", farmer, year).fetch().size() == 0) {
					result = checkAppleColor(farmer);
				}
			}
		}
		return result;
	}

	public static String checkAppleColor(Farmer farmer) {
		String result = "";
		String apples_type = SeedlingDao.getApplesColor(farmer);
		if (apples_type.equals(C.APPLE_COLOR_GOLD)) {
			result = EXTENSION_BIG_APPLES_GOLD;
		} else if (apples_type.equals(C.APPLE_COLOR_GREEN)) {
			result = EXTENSION_BIG_APPLES_GREEN;
		} else if (apples_type.equals(C.APPLE_COLOR_RED)) {
			result = EXTENSION_BIG_APPLES_RED;
		}
		return result;
	}

	public static String checkWhiteSprayed(Farmer farmer, int year_level) {
		String result = "";
		Item item = Item.find("byName", "BeloMaslo").first();
		List<ExecutedOperation> operations = ExecutedOperation.find(
				"byItemInstance.typeAndField", item, farmer.field).fetch();
		for (ExecutedOperation operation : operations) {
			if (DateDao.isSameYear(farmer,operation.startDate)) {
				if (DateDao.diffCurDate(farmer, operation.startDate) <= 60) {
					result = "b";
				}

			}
		}
		return result;
	}
}
