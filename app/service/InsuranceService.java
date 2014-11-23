package service;

import models.Farmer;
import models.OccurredDecease;
import exceptions.NotEnoughMoneyException;

public interface InsuranceService {
	
	public Farmer buyInsurance(Farmer farmer) throws NotEnoughMoneyException;
	
	public Boolean hasInsuranceThisYear(Farmer farmer);
	
	public Farmer refundInsurance(Farmer farmer, OccurredDecease odisease);

}
