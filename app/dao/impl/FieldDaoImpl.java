package dao.impl;

import dao.FieldDao;
import models.Farmer;
import models.ItemInstance;

public class FieldDaoImpl implements FieldDao{

	public Boolean hasTensiometerSystem(Farmer farmer) {
		ItemInstance tensiometerSystem = ItemInstance.find(
				"byType.nameAndownedBy", "tensiometer", farmer).first();
		if (tensiometerSystem == null) {
			return false;
		}
		return true;
	}
	
	public Boolean hasDropSystem(Farmer farmer) {
		ItemInstance dropsystem = ItemInstance.find("byType.nameAndownedBy",
				"KapkovoNavodnuvanje", farmer).first();
		if (dropsystem == null) {
			return false;
		}
		return true;
	}
}
