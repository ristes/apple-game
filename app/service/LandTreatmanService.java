package service;

import java.util.List;

import models.Farmer;
import models.LogFarmerData;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import exceptions.TypeOfPlowingNotRecognized;

public interface LandTreatmanService {

	public final static Integer DEEP_PLOW_OPTIMAL_VALUE = 70;
	public final static Integer SHALLOW_PLOW_OPTIMAL_VALUE = 20;
	public final static int LIMIT_PLOWING_TYPE = 45;
	public final static String PLOWING = "plowing";
	public final static String DEEP_PLOWING = "DeepPlowingItem";
	public final static String SHALLOW_PLOWING = "ShallowPlowingItem";
	public static final int SHALLOW_PLOWING_OPTIMAL_VALUE = 20;

	
	
	public Farmer executeDigging(Farmer farmer, Long item) throws NotEnoughMoneyException;
	public int diggingLevel(Farmer farmer);
	public int plowingLevel(Farmer farmer);
	public Farmer determineThePlowingPrice(Farmer farmer, Integer deep)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException, TypeOfPlowingNotRecognized;
	public Farmer executePlowing(Farmer farmer, Integer deep) throws TooWaterOnFieldException, SoilTooDryException, NotEnoughMoneyException, TypeOfPlowingNotRecognized;
	public void firstDeepPlowing(Farmer farmer, Integer deep);
	public Boolean isFirstDeepPlowed(Farmer farmer);
	public Boolean hasEcoTractor(Farmer farmer);
	public List<LogFarmerData> shallowPlowingOperationsForPrevRecolte(Farmer farmer);
	public List<LogFarmerData> deepPlowingOperationsForPrevRecolte(Farmer farmer);
	public Boolean hasDeepPlowed(Farmer farmer);
	public Boolean hasShallowPlowed(Farmer farmer);
	public void evalDiggingCoefs(Farmer farmer);
}
