package dao;

import models.Farmer;

public interface DeseaseRisk {
	public Double getRisk(Farmer context);
	public int getOperationsDiminushingFactor(Farmer context);
}
	