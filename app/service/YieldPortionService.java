package service;

import models.Farmer;
import models.Fridge;
import models.YieldPortion;

public interface YieldPortionService {

	
	
	/**
	 * @param quantity
	 * @return the rest of apples that the portions does not have 
	 */
	public Integer removeFromPortion(YieldPortion yieldportion, int quantity);
	
	public Boolean checkDeadline(Farmer farmer, Fridge fridge, YieldPortion portion) ;
}
