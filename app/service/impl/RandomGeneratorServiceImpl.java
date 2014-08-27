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

	@Override
	public Double random(Double minV, Double maxV) {
		Random random = new Random();
		return minV + random.nextDouble() * (maxV-minV);
	}

	@Override
	public Long random(Long minV, Long maxV) {
		Random random = new Random();
		Long randV = minV + (long)(random.nextDouble() * (maxV-minV));
		return randV;
	}

}
