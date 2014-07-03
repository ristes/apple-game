package dao;

import models.Farmer;

public class SeedlingDao {
	
	public static String getApplesColor(Farmer farmer) {
		return farmer.field.plantation.seadlings.type.apple_color;
	}

}
