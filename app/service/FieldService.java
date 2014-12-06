package service;

import models.Farmer;

public interface FieldService {
	
	public Boolean hasTensiometerSystem(Farmer farmer);
	public Boolean hasDropSystem(Farmer farmer);
	public Boolean hasBees(Farmer farmer);
	public Boolean hasUVProtectingNet(Farmer farmer);
	public Boolean hasArtificialRain(Farmer farmer);
}
