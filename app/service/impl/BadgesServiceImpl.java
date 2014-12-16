package service.impl;

import models.Badges;
import models.Farmer;
import service.BadgesService;
import service.ServiceInjector;
import service.YieldService;
import utils.JsonExcluder;

public class BadgesServiceImpl implements BadgesService{

	@Override
	public Badges yield(Farmer farmer, Integer harvested) {
		YieldService yieldService = new YieldServiceImpl();
		
		Badges badge = Badges.find("byAkka","yield").first();
		Double trigger = Double.parseDouble(JsonExcluder.byField(badge.metadata, "yield"));
		double total = yieldService.calculateYield(farmer);
		double per = (harvested / total)*100;
		
		if (per>trigger) {
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
		Badges badge = Badges.find("byAkka","harvester").first();
		Double trigger = Double.parseDouble(JsonExcluder.byField(badge.metadata, "yield_collector"));
		double per = (harvested / (double)yield)*100;
		if (per>trigger){
			return badge;
		} 
		return null;
	}
	

	@Override
	public Badges fertilizer(Farmer farmer) {
		Badges badge = Badges.find("byAkka","fertilizer").first();
		Double percent = Double.parseDouble(JsonExcluder.byField(badge.metadata, "fertilizer_hit"));
		Double percent_hits = ServiceInjector.fertilizeService.badgeEvaluate(farmer);
		if (percent_hits >= percent) {
			return badge;
		}
		return null;
	}

	@Override
	public Badges irrigator(Farmer farmer) {
		Badges badge = Badges.find("byAkka","irrigator").first();
		Integer trigger = Integer.parseInt(JsonExcluder.byField(badge.metadata, "irrigation_misses"));
		if (farmer.irrigation_misses<trigger) {
			return badge;
		}
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
