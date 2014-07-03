package dao;

import java.util.Calendar;
import java.util.Date;

import models.Day;
import models.Farmer;
import controllers.DeseasesExpertSystem;

public class DateDao {

	
	public static Date convertDateTo70(Date date) {
		Boolean afterNewYear = false;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (isAfterNewYear(date)) {
			c.set(Calendar.YEAR, 1971);
		} else {
			c.set(Calendar.YEAR, 1970);
		}
		return c.getTime();
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
	
	public static Boolean isSameYear(Farmer farmer, Date date) {

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
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			if (date.before(c.getTime())) {
				return true;
			}
		}
		return false;
	}
	
	public static Long diffCurDate(Farmer farmer, Date date) {
		Day day = Day.find("byDate", date).first();
		if (day==null) {
			return null;
		}
		return farmer.gameDate.dayOrder-day.dayOrder;
				
	}
}
