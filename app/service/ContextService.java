package service;

import java.util.Date;

import models.Farmer;

public interface ContextService {

	public static final Long AJDARET = 1L;
	public static final Long CRVEN_DELISHES = 2L;
	public static final Long ZLATEN_DELISHES = 3L;
	public static final Long JONALGOLD = 4L;
	public static final Long MUCU = 5L;
	public static final Long GRENI_SMIT =6L;
	
	
	public void calculateLuck(Farmer farmer);
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
	public double calculateHumidityLooses(Farmer farmer);
	public void setAndCheckLastLoginDate(Farmer farmer);
}
