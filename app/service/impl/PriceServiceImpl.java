package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dto.C;
import exceptions.PriceNotValidException;
import models.Farmer;
import service.PriceService;
import service.RandomGeneratorService;
import service.YmlService;

public class PriceServiceImpl implements PriceService{

	/*
	 * (non-Javadoc)
	 * @see service.PriceService#price(models.Farmer)
	 * y = a.*randn(31,1) + b; a=0.05; b=avg price
	 */
	@Override
	public Double price(Farmer farmer) throws PriceNotValidException{
		HashMap<Integer,ArrayList<Double>> monthPrice = YmlServiceImpl.load_hash_key_int(C.COEF_SALES_YML);
		Random random = new Random();
		Calendar calendar = Calendar.getInstance();
		RandomGeneratorService randomService = new RandomGeneratorServiceImpl();
		Double randV = randomService.randomGausseGenerator(31.0,1.0);
		Double avgPrice = monthPrice.get(farmer.field.plantation.seadlings.type.id.intValue()).get(calendar.get(Calendar.MONTH));
		if (avgPrice==0.0 || avgPrice==null) {
			throw new PriceNotValidException();
		}
		return 0.05*randV + avgPrice;
	}

}
