package dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import controllers.DeseasesExpertSystem;
import models.OperationBestTimeInterval;

public class FertilizerOperationDto {

	public Long id;
	public Long fertilizer_id;
	public Long operation_id;
	public Date startFrom;
	public Date endTo;
	public Double quantity;

	public static double sumOfQuantity(List<FertilizerOperationDto> list) {
		double sum = 0;
		for (FertilizerOperationDto fo : list) {
			sum += fo.quantity;
		}
		return sum;
	}
	
	
	public static Boolean isAfterNewYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		if (month <= Calendar.DECEMBER && month >= Calendar.OCTOBER) {
			return false;
		}
		if (month == Calendar.SEPTEMBER) {
			if (day > 15) {
				return false;
			}
		}
		return true;
	}
	
	public Boolean isInInterval(FertilizerOperationDto fert) {
		Calendar c = Calendar.getInstance();
		c.setTime(startFrom);
		if (isAfterNewYear(startFrom)) {
			c.set(Calendar.YEAR, 1971);
		} else {
			c.set(Calendar.YEAR, 1970);
		}
		if (fert.startFrom.before(startFrom)&&fert.endTo.after(startFrom)) {
			return true;
		}
		return false;
	}
	
//	public Boolean isInInterval(FertilizerOperationDto dto) {
//		
//		if (start)
//	}
	
//	public Boolean isInInterval(Date date) {
//		for (OperationBestTimeInterval interval : intervalsForPrevetions) {
//			if (interval.isInInterval(date)) {
//				return true;
//			}
//		}
//		return false;
//	}
}
