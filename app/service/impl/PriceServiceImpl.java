package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import models.Farmer;
import service.PriceService;
import service.ServiceInjector;
import dto.C;
import exceptions.PriceNotValidException;

public class PriceServiceImpl implements PriceService{

	/*
	 * (non-Javadoc)
	 * @see service.PriceService#price(models.Farmer)
	 * y = a.*randn(31,1) + b; a=0.05; b=avg price
	 */
	@Override
	public Double price(Farmer farmer) throws PriceNotValidException{
		HashMap<Integer,ArrayList<Double>> monthPrice = YmlServiceImpl.load_hash_key_int(C.COEF_SALES_YML);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(farmer.gameDate.date);
		Double randV = ServiceInjector.randomGeneratorService.randomGausseGenerator(31.0,1.0);
		Double avgPrice = monthPrice.get(farmer.field.plantation.seadlings.type.id.intValue()).get(calendar.get(Calendar.MONTH));
		if (avgPrice==0.0 || avgPrice==null) {
			throw new PriceNotValidException();
		}
		return 0.05*randV + avgPrice;
	}

}
