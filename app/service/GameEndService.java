package service;

import models.Farmer;

public interface GameEndService {
	
	public static final String STATUS_GAME_END = "game_ended";
	
	public void evaluate(Farmer farmer);

}
