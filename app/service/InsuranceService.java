package service;

import exceptions.NotEnoughMoneyException;
import models.Decease;
import models.Farmer;
import models.OccurredDecease;

public interface InsuranceService {
	
	public Farmer buyInsurance(Farmer farmer) throws NotEnoughMoneyException;
	
	public Boolean hasInsuranceThisYear(Farmer farmer);
	
	public Farmer refundInsurance(Farmer farmer, OccurredDecease odisease);

}
