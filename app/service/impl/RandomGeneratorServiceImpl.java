package service.impl;

import java.util.Random;

import service.RandomGeneratorService;

public class RandomGeneratorServiceImpl implements RandomGeneratorService{

	@Override
	public Double randomGausseGenerator(Double mean, Double variance) {
		Random random = new Random();
		Double randV = random.nextGaussian()*variance+mean;
		return randV;
	}

}
