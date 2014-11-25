package service;

import models.Farmer;

public interface HumidityService {

	public int looses_q(Farmer farmer, Double variation);
	public double looses_eco(double value, Double variation);
	public int humidityLevel(Farmer farmer);
	public Farmer humiditySetAppUrls(Farmer farmer);
	public int humidityLevelForSoil(int humidityLevel);
}
