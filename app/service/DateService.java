package service;

import java.util.Date;

import models.ExecutedOperation;
import models.Farmer;
import models.PlantType;

public interface DateService {
	

	public final static Integer YEAR_ORDINARY_FIRST = 1;
	public final static Integer YEAR_ORDINARY_SECOND = 2;
	
	public final static Integer SEASON_WINTER = 1;
	public final static Integer SEASON_SPRING = 3;
	public final static Integer SEASON_SUMMER = 4;
	public final static Integer SEASON_AUTUMN = 2;

	public Date convertDateTo70(Date date);
	public Boolean isAfterNewYear(Date date);
	public Boolean isSameYear(Farmer farmer, Date date);
	public Long diffCurDate(Farmer farmer, Date date);
	public ExecutedOperation changeYear(ExecutedOperation operation);
	public int season_level(Farmer farmer);
	public int monthLevel(Date date);
	public int evaluateYearLevel(Date date);
	public int evaluateYearLevel(int year);
	public int recolteYear(Date date);
	public int recolteYearByPlantType(Date date, PlantType type);
	public int getCalendarFieldOfDate(Date date, int fieldType);
	public int fridgerecolteyear(Date date);
	public Date convertFridgeDateTo70(Date date);
}
