package service;

import models.Disease;
import models.Farmer;

public interface LogFarmerDataService {
	
	public final static Integer CURRENT_YIELD = 1;
	public final static Integer ITEM_BOUGHT = 2;
	public final static Integer OPERATION_DONE = 3;
	public final static Integer MONEY_SPENT = 4;
	public final static Integer MONEY_EARNED = 5;
	public final static Integer APPLES_BURNT = 6;
	public final static Integer DISEASES_OCCURED = 7;
	
	
	public void logSetMaxYield(Farmer farmer, double yield);
	public void logMoneySpent(Farmer farmer, Integer ammount);
	public void logMoneyEarned(Farmer farmer, Integer ammount);
	public void logApplesBurned(Farmer farmer, Integer ammount);
	public void logOccurredDisease(Farmer farmer, Disease disease, Integer applesBurned);

}
