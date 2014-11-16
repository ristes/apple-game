package service.impl;

import models.Badges;
import models.Farmer;
import service.BadgesService;
import service.YieldService;

public class BadgesServiceImpl implements BadgesService{

	@Override
	public Badges yield(Farmer farmer, Integer harvested) {
		YieldService yieldService = new YieldServiceImpl();
		double total = yieldService.calculateYield(farmer);
		double per = (harvested / total)*100;
		
		if (per>90) {
			Badges badge = Badges.find("byAkka","yield").first();
			return badge;
		}
		return null;
	}

	@Override
	public Badges trader(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Badges harvester(Farmer farmer, Integer yield, Integer harvested) {
		double per = (harvested / (double)yield)*100;
		if (per>90){
			Badges badge = Badges.find("byAkka","harvester").first();
			return badge;
		} 
		return null;
	}

	@Override
	public Badges fertilizer(Farmer farmer, Double percent) {
		if (percent > 95) {
			Badges badge = Badges.find("byAkka","fertilizer").first();
			return badge;
		}
		return null;
	}

	@Override
	public Badges irrigator(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Badges ecologist(Farmer farmer) {
		if (farmer.eco_points>95) {
			Badges badge = Badges.find("byAkka","eco").first();
			return badge;
		}
		return null;
	}

}
