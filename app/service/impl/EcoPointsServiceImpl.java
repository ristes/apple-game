package service.impl;

import models.Farmer;
import service.EcoPointsService;

public class EcoPointsServiceImpl implements EcoPointsService{

	@Override
	public void add(Farmer farmer, Integer value) {
		if (value<0) {
			throw new NumberFormatException();
		}
		farmer.setEco_points(farmer.getEco_points()+value);
		
	}

	@Override
	public void substract(Farmer farmer, Integer value) {
		if (value<0) {
			throw new NumberFormatException();
		}
		farmer.setEco_points(farmer.getEco_points()-value);
		
	}

	@Override
	public void divide(Farmer farmer, Double value) {
		if (value<=0) {
			throw new NumberFormatException();
		}
		farmer.setEco_points(farmer.getEco_points()+farmer.getEco_points()/value);
		
	}

	@Override
	public void restart(Farmer farmer) {
		farmer.setEco_points(ECO_START_VALUE);
	}

}
