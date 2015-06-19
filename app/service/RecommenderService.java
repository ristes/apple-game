package service;

import java.util.List;

import models.Farmer;
import dto.FertilizationItem;

public interface RecommenderService {
	
	public List<FertilizationItem> fertilize(Farmer farmer);
	

}
