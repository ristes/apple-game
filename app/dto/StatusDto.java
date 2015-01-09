package dto;

import models.Farmer;

public class StatusDto<T> {
	
	public Boolean status;
	
	public String message;
	
	public Farmer farmer;
	
	public String additionalInfo;
	
	public String tip;
	
	public T t;
	
	public StatusDto(Boolean status) {
		this.status = status;
	}
	
	public StatusDto(Boolean status, String message, String additionalInfo, Farmer farmer) {
		this.status =status;
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.farmer = farmer;
	}
	public StatusDto(Boolean status, String message, String additionalInfo, Farmer farmer, T t) {
		this.status =status;
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.farmer = farmer;
		this.t = t;
	}

}
