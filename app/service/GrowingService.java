package service;

import models.Farmer;

public interface GrowingService {

	public String evaluatePlantImage(Farmer farmer);
	public int year_tree_image(int year_level);
	public String checkToPutApplesOnTree(Farmer farmer, int year,
			int year_level, int month, int season);
	public String checkAppleColor(Farmer farmer);
	public String checkWhiteSprayed(Farmer farmer, int year_level);
}
