package service;

import java.util.List;

import models.Farmer;
import models.FarmerPoints;

public interface RankingService {

	
	public List<FarmerPoints> rank(Integer year);
	public Farmer savePoints(Farmer farmer, Integer ecoPoints, Integer applesHarvested, Integer moneyEarned);
}
