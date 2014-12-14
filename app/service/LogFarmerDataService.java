package service;

import java.util.Date;

import models.Disease;
import models.Farmer;
import models.Operation;

public interface LogFarmerDataService {
	
	public final static Long MAX_YIELD = 0l;
	public final static Long CURRENT_YIELD = 1l;
	public final static Long ITEM_BOUGHT = 2l;
	public final static Long OPERATION_DONE = 3l;
	public final static Long MONEY_SPENT = 4l;
	public final static Long MONEY_EARNED = 5l;
	public final static Long APPLES_BURNT = 6l;
	public final static Long DISEASES_OCCURED = 7l;
	public final static Long OPERATION_EXECUTED = 8l;
	public final static Long APPLES_SOLD = 9l;
	
	
	public void logSetMaxYield(Farmer farmer,  double yield);
	public void logMoneySpent(Farmer farmer,  Integer ammount);
	public void logMoneyEarned(Farmer farmer, Integer ammount);
	public void logApplesBurned(Farmer farmer, Integer ammount);
	public void logOccurredDisease(Farmer farmer, Disease disease, Integer applesBurned);
	public void logExecutedOperation(Farmer farmer,  Operation operation);
	public void logApplesSold(Farmer farmer, Integer quantity);

}
