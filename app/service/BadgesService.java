package service;

import models.Badges;
import models.Farmer;

public interface BadgesService {
	
	public static final double BADGE_TRADER_THRESHOLDER = 0.85;
	public static final Integer BADGE_ECO_THRESHOLDER = 95;
	public static final Integer BADGE_EXECUTED_OPERATIONS = 10;
	
	public Badges yield(Farmer farmer, Integer harvested);
	
	public Badges trader(Farmer farmer);
	
	public Badges harvester(Farmer farmer, Double percentCollected);
	
	public Badges fertilizer(Farmer farmer);
	
	public Badges irrigator(Farmer farmer);
	
	public Badges ecologist(Farmer farmer);

}
