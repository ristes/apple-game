package dao.impl;

import models.Farmer;
import models.ItemInstance;
import dao.FieldDao;

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
	
	public Boolean hasBees(Farmer farmer) {
		ItemInstance bees = ItemInstance.find("byType.nameAndownedBy",
				"Bees", farmer).first();
		if (bees == null) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean hasUVProtectingNet(Farmer farmer) {
		ItemInstance uv_prot_net = ItemInstance.find("byType.nameAndownedBy", "uv_protecting_net", farmer).first();
		if (uv_prot_net == null) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean hasArtificalRain(Farmer farmer) {
		ItemInstance artificial_rain = ItemInstance.find("byType.nameAndownedBy", "artificial_rain", farmer).first();
		if (artificial_rain == null) {
			return false;
		}
		return true;
	}
}
