package service;

import models.Farmer;
import dto.ItemBoughtDto;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public interface IrrigationService {
	
	public static final Integer MAX_IRRIGATION_VALUE = 45;
	public static final Integer MIN_IRRIGATION_VALUE = -25;

	public double dropsIrrigation(Farmer farmer, Integer time)
			throws NotEnoughMoneyException;

	public double groovesIrrigation(Farmer farmer, Integer time)
			throws NotEnoughMoneyException;

	public int tensiometerTimeForIrr(Farmer farmer) throws NotAllowedException;

	public ItemBoughtDto getActiveIrrigationType(Farmer farmer);
	
}
