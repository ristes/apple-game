package service;

import models.Farmer;

public interface LuckService {
	
	public void generateLuck(Farmer farmer);
	
	public double getLuck(Farmer farmer);

}
