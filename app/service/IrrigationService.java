package service;

import models.Farmer;
import exceptions.NotAllowedException;
import exceptions.NotEnoughMoneyException;

public interface IrrigationService {

	public double dropsIrrigation(Farmer farmer, Integer time) throws NotEnoughMoneyException;
	public double groovesIrrigation(Farmer farmer, Integer time) throws NotEnoughMoneyException;
	public int tensiometerTimeForIrr(Farmer farmer,int irrigationType) throws NotAllowedException;
}
