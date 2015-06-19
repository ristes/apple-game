package service.impl;

import java.util.Calendar;
import java.util.Date;

import models.Day;
import models.ExecutedOperation;
import models.Farmer;
import models.HarvestingPeriod;
import models.PlantType;
import service.DateService;
import service.ServiceInjector;

public class DateServiceImpl implements DateService{

	public Date convertDateTo70(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterNewYear(date)) {
			c.set(Calendar.YEAR, 1971);
		} else {
			c.set(Calendar.YEAR, 1970);
		}
		return c.getTime();
	}
	
	public Boolean isAfterNewYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		if (month >= Calendar.NOVEMBER) {
			return false;
		}
		
		return true;
	}
	
	public Boolean isAfterFridgeNewYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		if (month >= Calendar.SEPTEMBER) {
			return false;
		}
		
		return true;
	}
	
	public int recolteYearByPlantType(Date date, PlantType type) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int monthCur = cal.get(Calendar.MONTH);
		int dayCur = cal.get(Calendar.DAY_OF_MONTH);
		int yearCur = cal.get(Calendar.YEAR);
		cal.setTime(type.period.startFrom);
		int monthStart = cal.get(Calendar.MONTH);
		int dayStart = cal.get(Calendar.DAY_OF_MONTH);
		if (monthCur<monthStart) {
			return yearCur - 1;
		} else if (monthCur==monthStart) {
			if (dayCur<dayStart) {
				return yearCur-1;
			} else {
				return yearCur;
			}
		}
		return yearCur;
	}
	
	/**
	 * 
	 * @param farmer
	 * @param date
	 * @return the year of recolte
	 */
	public int recolteYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterNewYear(date)) {
			return c.get(Calendar.YEAR) - 1;
		}
		return c.get(Calendar.YEAR);
	}
	
	public int monthLevel(Date date) {
		Calendar c= Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH)+1;
	}
	
	public Boolean isSameYear(Farmer farmer, Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		if (isAfterNewYear(c.getTime())) {
			c.add(Calendar.YEAR, -1);
		}
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MONTH, Calendar.OCTOBER);

		if (date.after(c.getTime())) {
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			if (date.before(c.getTime())) {
				return true;
			}
		}
		return false;
	}
	
	public Long diffCurDate(Farmer farmer, Date date) {
		Day day = Day.find("byDate", date).first();
		if (day==null) {
			return null;
		}
		return farmer.gameDate.dayOrder-day.dayOrder;
				
	}
	
	public ExecutedOperation changeYear(ExecutedOperation operation) {
		ExecutedOperation result = operation.clone();
		Calendar c = Calendar.getInstance();
		c.setTime(result.startDate);
		c.set(Calendar.YEAR, 1970);
		if (ServiceInjector.dateService.isAfterNewYear(c.getTime())) {
			c.add(Calendar.YEAR, 1);
		}
		result.startDate = c.getTime();
		return result;
	}
	
	/**
	 * describes the weather season depending of gamedate of the farmer state 
	 * 2. Autumn - 01.10 - 30.11 
	 * 1. Winter - 01.12 - 28.02 
	 * 3. Spring - 01.03 - 30.04 
	 * 4. Summer - 01.05 - 30.09
	 * 
	 * @param farmer
	 */
	public int season_level(Farmer farmer) {
//		Calendar c = Calendar.getInstance();
//		c.setTime(farmer.gameDate.date);
//		int month = c.get(Calendar.MONTH);
		int month = getCalendarFieldOfDate(farmer.gameDate.date, Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
			return DateService.SEASON_WINTER;
		case Calendar.FEBRUARY:
			return DateService.SEASON_WINTER;
		case Calendar.MARCH:
			return DateService.SEASON_SPRING;
		case Calendar.APRIL:
			return DateService.SEASON_SPRING;
		case Calendar.MAY:
			return DateService.SEASON_SPRING;
		case Calendar.JUNE:
			return DateService.SEASON_SUMMER;
		case Calendar.JULY:
			return DateService.SEASON_SUMMER;
		case Calendar.AUGUST:
			return DateService.SEASON_SUMMER;
		case Calendar.SEPTEMBER:
			return DateService.SEASON_AUTUMN;
		case Calendar.OCTOBER:
			return DateService.SEASON_AUTUMN;
		case Calendar.NOVEMBER:
			return DateService.SEASON_AUTUMN;
		case Calendar.DECEMBER:
			return DateService.SEASON_WINTER;
		}
		return -1;
	}
//	@Override
	public int evaluateYearLevel(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		return evaluateYearLevel(year);
	}

//	@Override
	public int evaluateYearLevel(int year) {
		int year_level = 0;
		switch (year) {
		case 2020:
			year_level = 1;
			break;
		case 2021:
			year_level = 2;
			break;
		case 2022:
			year_level = 3;
			break;
		case 2023:
			year_level = 4;
			break;
		case 2024:
			year_level = 5;
			break;
		default:
			year_level = 5;
		}
		return year_level;
	}

	@Override
	public int getCalendarFieldOfDate(Date date, int fieldType) {
		Calendar c = Calendar.getInstance();
		if (date==null) {
			return 0;
		}
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}

	@Override
	public int fridgerecolteyear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterFridgeNewYear(date)) {
			return c.get(Calendar.YEAR) - 2;
		}
		return c.get(Calendar.YEAR)-1;	}

	@Override
	public Date convertFridgeDateTo70(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterFridgeNewYear(date)) {
			c.set(Calendar.YEAR, 1971);
		} else {
			c.set(Calendar.YEAR, 1970);
		}
		return c.getTime();
	}

	@Override
	public Integer evaluateYearOrder(int recolteYear) {
		return recolteYear-2020;
	}
	
}
