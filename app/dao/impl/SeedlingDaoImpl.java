package dao.impl;

import models.Farmer;
import dao.SeedlingDao;

public class SeedlingDaoImpl implements SeedlingDao{

	@Deprecated
	public String getApplesColor(Farmer farmer) {
		return farmer.field.plantation.seadlings.type.apple_color;
	}
	
	
}
