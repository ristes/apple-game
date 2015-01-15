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
	

	
	
	public void calculateCumulatives(Farmer farmer);
	public void evaluateSoilImage(Farmer farmer);
	public void evaluateSeason(Farmer farmer);
	public void evaluateRestartState(Farmer farmer);
	public void evaluateFertilizingState(Farmer farmer);
	public void evaluateState(Farmer farmer);
	public void evaluateUV(Farmer farmer);
	public void evaluateLowTemps(Farmer farmer);
	public void onLoadEvaluateState(Farmer farmer);
	public void calculateFertalizing(Farmer farmer);
	public int seasionLevelSoilImage(int season_level);
	public void setAndCheckLastLoginDate(Farmer farmer);
	public void evaluateFridgesState(Farmer farmer);
}
