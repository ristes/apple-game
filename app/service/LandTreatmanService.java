package service;

import java.util.Map;

import models.Farmer;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;
import exceptions.TypeOfPlowingNotRecognized;

public interface LandTreatmanService {

	
	public final static String DEEP_PLOWING = "DeepPlowingItem";
	public final static String SHALLOW_PLOWING = "ShallowPlowingItem";

	
	
	public Farmer executeDigging(Farmer farmer, Long item) throws NotEnoughMoneyException;
	public int diggingLevel(Farmer farmer);
	public int plowingLevel(Farmer farmer);
	public Farmer determineThePlowingPrice(Farmer farmer, Integer deep)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException, TypeOfPlowingNotRecognized;
	public Farmer executePlowing(Farmer farmer, Integer deep) throws TooWaterOnFieldException, SoilTooDryException, NotEnoughMoneyException, TypeOfPlowingNotRecognized;
	public Boolean hasEcoTractor(Farmer farmer);
}
