package dao;

import models.Farmer;

public interface FieldDao {

	public Boolean hasTensiometerSystem(Farmer farmer);
	public Boolean hasDropSystem(Farmer farmer);
	public Boolean hasBees(Farmer farmer);
	public Boolean hasUVProtectingNet(Farmer farmer);
	public Boolean hasArtificalRain(Farmer farmer);
}
