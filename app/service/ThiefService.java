package service;

import models.Farmer;

public interface ThiefService {
	
	public Boolean hasProtectingNet(Farmer farmer);
	public void checkThiefProb(Farmer farmer);

}
