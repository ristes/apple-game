package service.impl;

import java.util.Calendar;
import java.util.List;

import models.Badges;
import models.Farmer;
import models.LogFarmerData;
import service.BadgesService;
import service.LogFarmerDataService;
import service.ServiceInjector;
import service.YieldService;
import utils.JsonExcluder;

public class BadgesServiceImpl implements BadgesService{

	@Override
	public Badges yield(Farmer farmer, Integer harvested) {
		
		Badges badge = Badges.find("byAkka","yield").first();
		Double trigger = Double.parseDouble(JsonExcluder.byField(badge.metadata, "yield"));
		double total = ServiceInjector.yieldService.getMaxYieldByRecolte(farmer, ServiceInjector.dateService.recolteYear(farmer.gameDate.date));
		double per = (harvested / total)*100;
		if (per>trigger) {
			return badge;
		}
		return null;
	}

	@Override
	public Badges trader(Farmer farmer) {
		List<LogFarmerData> logs = LogFarmerData.find("byTypelog",LogFarmerDataService.APPLES_SOLD).fetch();
		List<LogFarmerData> logsBurned = LogFarmerData.find("byTypelog", LogFarmerDataService.APPLES_BURNED_IN_FRIDGE).fetch();
		int total = logs.size()+logsBurned.size();
		int success = 0;
		for (LogFarmerData log:logs) {
			if (ServiceInjector.dateService.getCalendarFieldOfDate(log.logdate, Calendar.MONTH)==Calendar.JUNE) {
				success++;
			}
 		}
		if ((success/(double)total)>BadgesService.BADGE_TRADER_THRESHOLDER) {
			return Badges.find("byAkka","trader").first();
		}
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
