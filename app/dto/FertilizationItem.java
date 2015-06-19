package dto;

import models.OperationBestTimeInterval;

public class FertilizationItem {
	
	public FertilizationItem() {
		
	}
	
	public FertilizationItem(OperationBestTimeInterval ins) {
		this.name = ins.fertilizationBestTime.fertilizer.name;
		this.value = ins.quantity;
	}

	public String name;
	
	public Double value;

}
