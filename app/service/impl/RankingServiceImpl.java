package service.impl;

import java.util.List;

import models.Farmer;
import models.FarmerPoints;
import service.RankingService;
import service.ServiceInjector;

public class RankingServiceImpl implements RankingService{

	@Override
	public List<FarmerPoints> rank(Integer year) {
		List<FarmerPoints> result = FarmerPoints.find("SELECT fp FROM FarmerPoints fp where fp.year=?1 ORDER BY fp.totalPoints DESC",year).fetch(10);
		return result;
	}

	@Override
	public Farmer savePoints(Farmer farmer, Integer ecoPoints,
			Integer applesHarvested, Integer moneyEarned) {
		FarmerPoints points = new FarmerPoints();
		points.setFarmer(farmer);
		points.setEcoPoints(ecoPoints);
		points.setHarvestQuantity(applesHarvested);
		points.setMoneyEarned(moneyEarned);
		points.setYear(ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
		points.setTotalPoints(calculate(ecoPoints,applesHarvested, moneyEarned));
		points.save();
		return farmer;
	}
	
	private Integer calculate(Integer ecoPoints,
			Integer applesHarvested, Integer moneyEarned) {
		return ecoPoints*100 + applesHarvested + moneyEarned;
	}

}
