package dao.impl;

import dao.SeedlingDao;
import models.Farmer;

public class SeedlingDaoImpl implements SeedlingDao{

	@Deprecated
	public String getApplesColor(Farmer farmer) {
		return farmer.field.plantation.seadlings.type.apple_color;
	}
	
	
}
