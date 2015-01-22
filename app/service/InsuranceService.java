package service;

import models.Farmer;
import models.ItemInstance;
import models.OccurredDecease;
import exceptions.NotEnoughMoneyException;

public interface InsuranceService {
	
	public ItemInstance buyInsurance(Farmer farmer) throws NotEnoughMoneyException;
	
	public Boolean hasInsuranceThisYear(Farmer farmer);
	
	public Farmer refundInsurance(Farmer farmer, OccurredDecease odisease);

}
