package service;

public interface RandomGeneratorService {

	public Long random(Long minV, Long maxV);
	public Double random(Double minV, Double maxV);
	public Double randomGausseGenerator(Double mean, Double variance);
}
