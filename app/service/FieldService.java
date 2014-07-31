package service;

import models.Farmer;

public interface FieldService {
	
	public Boolean hasTensiometerSystem(Farmer farmer);
	public Boolean hasDropSystem(Farmer farmer);

}
