package dto;

import models.PlantType;
import models.SeedlingType;

public class HarvestingInfo {

	public String iodineStarchUrl;

	private double iodineStarch;

	public double strength;

	public double rfValue;

	public PlantType type;

	public Long plantationSeedlignId;

	public double getIodineStarch() {
		return iodineStarch;
	}

	public void setIodineStarch(double iodineStarch) {
		if (iodineStarch<0) {
			this.iodineStarch = 0.0;
		} else {
			this.iodineStarch = iodineStarch;
		}
	}
	
	
}
