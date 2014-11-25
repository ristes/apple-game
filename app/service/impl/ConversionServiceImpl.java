package service.impl;

import service.MoneyConversionService;

public class ConversionServiceImpl implements MoneyConversionService{
	
	public static final Double EURO_VALUE = 60.0;

	@Override
	public double toEuros(double dens) {
		return dens/EURO_VALUE;
	}

	@Override
	public double toDens(double euros) {
		return euros*EURO_VALUE;
	}

}
