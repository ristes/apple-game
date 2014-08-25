package service;

import java.util.List;

import dto.ItemBoughtDto;
import models.Farmer;

public interface FarmerService {
	
	public static final String STATE_GROWING = "growing";

	public double subtractEcoPoints(Farmer farmer, double points);
	public Farmer gotoNextDay(Farmer farmer);
	public Farmer gotoNextWeek(Farmer farmer);
	public Farmer gotoNextMonth(Farmer farmer);
	public Farmer buildInstance(String username, String password);
	public Farmer buildFbInstance(String username, String access_token, String name, String surname, String email, String picture);
	public Farmer addCumulative(Farmer farmer, double deltaCumulative);
	public Farmer setState(Farmer farmer, String state);
	public List<ItemBoughtDto> farmersItems(Farmer farmer);
}
