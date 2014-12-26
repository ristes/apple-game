package service;

import java.util.Map;

import models.Badges;
import models.Farmer;
import dto.ItemBoughtDto;

public interface FarmerService {
	
	public static final String STATE_GROWING = "growing";

	public double subtractEcoPoints(Farmer farmer, double points);
	public Double subtractProductQuantity(Farmer farmer, double percent, Boolean showInfoTable, String reason);
	public Farmer gotoNextDay(Farmer farmer);
	public Farmer gotoNextWeek(Farmer farmer);
	public Farmer gotoNextMonth(Farmer farmer);
	public Farmer buildInstance(String username, String password);
	public Farmer buildFbInstance(String username, String access_token, String name, String surname, String email, String picture);
	public Farmer addCumulative(Farmer farmer, double deltaCumulative);
	public Farmer setState(Farmer farmer, String state);
	public Map<String,ItemBoughtDto> farmersItems(Farmer farmer);
	public Farmer restartGame(Farmer farmer);
	public Farmer collectBadge(Farmer farmer, Badges badge);
	public boolean hasBees(Farmer farmer);
}
