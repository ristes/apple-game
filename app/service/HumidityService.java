package service;

import models.Farmer;

public interface HumidityService {

	public void looses_q_more_water(Farmer farmer, Double variation);
	public void looses_q_less_water(Farmer farmer, Double variation);
	public void looses_eco(Farmer farmer, Double variation);
	public int humidityLevel(Farmer farmer);
	public Farmer humiditySetAppUrls(Farmer farmer);
	public int humidityLevelForSoil(int humidityLevel);
}
