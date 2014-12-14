package service;

import models.Farmer;
import dto.ResumeMessageDto;

public interface ResumeService {
	
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
	
	public ResumeMessageDto moneySpent(Farmer farmer, Integer year);
	public ResumeMessageDto moneyEarned(Farmer farmer, Integer year);
	public ResumeMessageDto operationsExecuted(Farmer farmer, Integer year);
	public ResumeMessageDto diseasesOccured(Farmer farmer, Integer year);
	public ResumeMessageDto applesSold(Farmer farmer, Integer year); 
	public ResumeMessageDto applesBurned(Farmer farmer, Integer year);

}
