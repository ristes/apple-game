package service;

import java.util.Date;

import models.ExecutedOperation;
import models.Farmer;
import models.PlantType;

public interface DateService {

	public Date convertDateTo70(Date date);
	public Boolean isAfterNewYear(Date date);
	public Boolean isSameYear(Farmer farmer, Date date);
	public Long diffCurDate(Farmer farmer, Date date);
	public ExecutedOperation changeYear(ExecutedOperation operation);
	public int season_level(Farmer farmer);
	public int evaluateYearLevel(Date date);
	public int evaluateYearLevel(int year);
	public int recolteYear(Date date);
	public int recolteYearByPlantType(Date date, PlantType type);
}
