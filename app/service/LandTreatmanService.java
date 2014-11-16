package service;

import models.Farmer;
import models.Item;
import exceptions.NotEnoughMoneyException;
import exceptions.SoilTooDryException;
import exceptions.TooWaterOnFieldException;

public interface LandTreatmanService {

	public Farmer executeDigging(Farmer farmer, Long item) throws NotEnoughMoneyException;
	public int diggingLevel(Farmer farmer);
	public int plowingLevel(Farmer farmer);
	public Farmer determineThePlowingPrice(Farmer farmer)
			throws SoilTooDryException, TooWaterOnFieldException, NotEnoughMoneyException;
	public Farmer executePlowing(Farmer farmer) throws TooWaterOnFieldException, SoilTooDryException, NotEnoughMoneyException;
	public Boolean hasEcoTractor(Farmer farmer);
}
