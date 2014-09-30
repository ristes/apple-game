package service;

import java.util.Date;

import models.Farmer;

public interface ContextService {

	public void calculateLuck(Farmer farmer);
	public double calculateHumidityLooses(Farmer farmer);
	public void calculateCumulatives(Farmer farmer);
	public void calculateGrassGrowth(Farmer farmer);
	public void calculateDiggingCoefficient(Farmer farmer);
	public void evaluateSoilImage(Farmer farmer);
	public void evaluatePlantImage(Farmer farmer);
	public void evaluateSeason(Farmer farmer);
	public void evaluateDisease(Farmer farmer);
	public void evaluateRestartState(Farmer farmer);
	public void evaluateFertilizingState(Farmer farmer);
	public void evaluateState(Farmer farmer);
	public void calculateFertalizing(Farmer farmer);
	public int seasionLevelSoilImage(int season_level);
	public Double rainCoefForMonth(Date date);
	public Double rainCoefForMonth(Integer month);
}
