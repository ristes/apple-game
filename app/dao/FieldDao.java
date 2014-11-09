package dao;

import models.Farmer;

public interface FieldDao {

	public Boolean hasTensiometerSystem(Farmer farmer);
	public Boolean hasDropSystem(Farmer farmer);
	public Boolean hasBees(Farmer farmer);
}
