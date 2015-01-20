package service.impl;

import java.util.Calendar;
import java.util.List;

import models.ExecutedOperation;
import models.Farmer;
import models.Item;
import models.PlantType;
import models.Yield;
import service.GrowingService;
import service.ServiceInjector;
import dto.C;

public class GrowingServiceImpl implements GrowingService {

	public final static String EXTENSION_BIG_APPLES_GREEN = "abgr";
	public final static String EXTENSION_BIG_APPLES_RED = "abr";
	public final static String EXTENSION_BIG_APPLES_GOLD = "abg";
	public final static String EXTENSION_SMALL_APPLES = "as";

	public final static String EXTENSION_WHITE_TREE = "b";

	public String evaluatePlantImage(Farmer farmer, Long plantType) {
		String image_path = "/public/images/game/apple_tree/";
		StringBuilder additional = new StringBuilder();
		int season = ServiceInjector.dateService.season_level(farmer);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(farmer.gameDate.date);
		int year_level = 1;
		int recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		year_level = year_tree_image(ServiceInjector.dateService
				.evaluateYearLevel(farmer.gameDate.date));

		additional.append(checkToPutApplesOnTree(farmer, recolteYear, year_level,
				month, season, plantType));
		additional.append(checkWhiteSprayed(farmer, year_level));
		return image_path + String.valueOf(year_level) + String.valueOf(season)
				+ additional + ".png";
	}

	public int year_tree_image(int year_level) {
		if (year_level >= 3) {
			return 3;
		}
		return year_level;
	}

	public String checkToPutApplesOnTree(Farmer farmer, Integer year,
			int year_level, int month, int season, Long plantType) {
		String result = "";
		if (year_level == 3) {
			if (ServiceInjector.harvestService.isInHarvestingPeriod(farmer,
					plantType)) {
				// has been harvested this year to show apples on plant
				if (Yield
						.find("byFarmerAndYearAndPlantationSeedling.seedling.type.id",
								farmer, year, plantType).fetch().size() == 0) {
					result = checkAppleColor(farmer, plantType);
					
				}
			} else {
				if (month == Calendar.AUGUST || month == Calendar.SEPTEMBER
						|| month == Calendar.OCTOBER) {
					if (Yield
							.find("byFarmerAndYearAndPlantationSeedling.seedling.type.id",
									farmer, year, plantType).fetch().size() == 0) {
						if (!ServiceInjector.harvestService.isAfterHarvestingPeriod(farmer, plantType)) {
							
							result = EXTENSION_SMALL_APPLES;
						}
						
					}
				}
			}
		}
		return result;
	}

	public String checkAppleColor(Farmer farmer, Long plantType) {
		String result = "";
		PlantType plantTypeIns = PlantType.findById(plantType);
		String color = plantTypeIns.apple_color;
		if (color.equals(C.APPLE_COLOR_GOLD)) {
			result = EXTENSION_BIG_APPLES_GOLD;
		} else if (color.equals(C.APPLE_COLOR_GREEN)) {
			result = EXTENSION_BIG_APPLES_GREEN;
		} else if (color.equals(C.APPLE_COLOR_RED)) {
			result = EXTENSION_BIG_APPLES_RED;
		}
		return result;
	}

	public String checkWhiteSprayed(Farmer farmer, int year_level) {
		String result = "";
		Item item = Item.find("byName", "BeloMaslo").first();
		List<ExecutedOperation> operations = ExecutedOperation.find(
				"byItemInstance.typeAndField", item, farmer.field).fetch();
		for (ExecutedOperation operation : operations) {
			if (ServiceInjector.dateService.isSameYear(farmer,
					operation.startDate)) {
				if (ServiceInjector.dateService.diffCurDate(farmer,
						operation.startDate) <= 60) {
					result = "b";
				}

			}
		}
		return result;
	}

}
