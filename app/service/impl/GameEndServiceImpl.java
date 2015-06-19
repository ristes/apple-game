package service.impl;

import models.Farmer;
import service.GameEndService;

public class GameEndServiceImpl implements GameEndService{

	@Override
	public void evaluate(Farmer farmer) {
		if (farmer.getEco_points()<=0) {
			farmer.setStatus(STATUS_GAME_END);
		}
		if (farmer.getBalance()<=0.0) {
			farmer.setStatus(STATUS_GAME_END);
		}
		if (farmer.gameDate.dayOrder.equals(11314l)) {
			farmer.setStatus(STATUS_GAME_END);
		}
	}

}
