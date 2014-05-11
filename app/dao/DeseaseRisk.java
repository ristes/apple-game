package dao;

import models.Farmer;

public interface DeseaseRisk {
	public Double getRisk(Farmer context);
	public Double getOperationsDiminushing(Farmer context);
}
	