package service;

import java.util.List;

import models.Disease;
import models.ExecutedOperation;
import models.Farmer;
import models.LogFarmerData;
import models.Operation;
import dto.QuizResultsDto;

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
	public final static long APPLES_BURNED_IN_FRIDGE = 10l;
	public final static Long QUIZ_ANSWERED = 11l;
	public final static Long RAIN_VALUE = 12l;
	public final static Long ECO_END_SEASON =13l;
	
	
	public void logSetMaxYield(Farmer farmer,  double yield);
	public void logMoneySpent(Farmer farmer,  Integer ammount);
	public void logMoneyEarned(Farmer farmer, Integer ammount);
	public void logApplesBurned(Farmer farmer, Integer ammount);
	public void logApplesBurnedInFridge(Farmer farmer, Integer ammount);
	public void logOccurredDisease(Farmer farmer, Disease disease, Integer applesBurned);
	public void logExecutedOperation(Farmer farmer,  Operation operation, Double deep);
	public void logApplesSold(Farmer farmer, Integer quantity);
	public void logQuizAnswered(Farmer farmer, QuizResultsDto answer);
	public void logRainValue(Farmer farmer, Double value);
	public void logFinalEcoPoints(Farmer farmer, Double value);
	
	public LogFarmerData hasExecutedOperation(Farmer farmer, String operation, Integer year);
	
	public List<LogFarmerData> getFarmerExecutedOperationsForYear(Farmer farmer, Integer year);
	
	

}
