package service;

import models.Badges;
import models.Farmer;

public interface BadgesService {
	
	public Badges yield(Farmer farmer, Integer harvested);
	
	public Badges trader(Farmer farmer);
	
	public Badges harvester(Farmer farmer, Integer yield, Integer harvested);
	
	public Badges fertilizer(Farmer farmer, Double percent);
	
	public Badges irrigator(Farmer farmer);
	
	public Badges ecologist(Farmer farmer);

}
