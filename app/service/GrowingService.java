package service;

import models.Farmer;
import models.PlantType;

public interface GrowingService {

	public String evaluatePlantImage(Farmer farmer, Long plantType);
	public int year_tree_image(int year_level);
	public String checkToPutApplesOnTree(Farmer farmer, Integer year,
			int year_level, int month, int season, Long plantType);
	public String checkAppleColor(Farmer farmer, Long plantType);
	public String checkWhiteSprayed(Farmer farmer, int year_level);
}
