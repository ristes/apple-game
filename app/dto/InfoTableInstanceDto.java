package dto;

import models.InfoTableInstance;

public class InfoTableInstanceDto {
	
	public Boolean isRead;
	
	public String message1;
	public String message2;
	public String image2_url;
	public String image3_url;
	
	public InfoTableInstanceDto() {
		
	}
	
	public InfoTableInstanceDto(InfoTableInstance ins) {
		this.isRead = ins.isRead;
		this.message1 = ins.message1;
		this.message2 = ins.message2;
		this.image2_url = ins.image2_url;
		this.image3_url = ins.image3_url;
	}

}
