package service;

import models.Farmer;
import models.LogFarmerData;

public interface PrunningService {
	
	public static final String OPERATION_NAME = "prunning";
	
	public void prune(Farmer farmer, Integer good, Integer totalgood);
	
	public LogFarmerData hasPruned(Farmer farmer);
	
	public double getDiminusher(Farmer farmer, LogFarmerData log);

}
