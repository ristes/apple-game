package service.impl;

import java.util.Random;

import models.Farmer;
import service.LuckService;

public class LuckServiceImpl implements LuckService{

	@Override
	public void generateLuck(Farmer farmer) {
		if (farmer.gameDate.dayOrder % 5 == 0) {
			farmer.luck = getLuck(farmer);
		}
	}

	@Override
	public double getLuck(Farmer farmer) {
		Double luck = 0.0;
		Random random = new Random();
		Double stand_dev = farmer.luck_dev;
		Double avg = farmer.luck_avg;
		luck = (random.nextGaussian() * stand_dev + avg);
		if (luck < (avg - stand_dev)) {
			luck = avg - stand_dev;
		}
		return luck;
	}

}
