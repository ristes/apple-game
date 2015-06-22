package dto;

import java.util.List;

import models.Farmer;
import models.InfoTable;
import models.LearnStateEvents;

public class StatusDto<T> {
	
	public Boolean status;
	
	public String message;
	
	public Farmer farmer;
	
	public String additionalInfo;
	
	public String tip;
	
	public LearnStateEvents event;
	
	public List<InfoTableInstanceDto> infoTables;
	
	public T t;
	
	public StatusDto(Boolean status) {
		this.status = status;
	}
	
	public StatusDto(Boolean status, String message, String additionalInfo, Farmer farmer, List<InfoTableInstanceDto> infoTables) {
		this.status =status;
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.farmer = farmer;
		this.infoTables = infoTables;
	}
	public StatusDto(Boolean status, String message, String additionalInfo, Farmer farmer, T t, List<InfoTableInstanceDto> infoTables) {
		this.status =status;
		this.message = message;
		this.additionalInfo = additionalInfo;
		this.farmer = farmer;
		this.infoTables = infoTables;
		this.t = t;
	}

}
