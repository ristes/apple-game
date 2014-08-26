package service;

import models.Farmer;
import models.InfoTable;
import models.InfoTableInstance;

public interface InfoTableService {

	
	public void createT1(Farmer farmer, String message1, String image2_url);
	public void createT2(Farmer farmer, String message1, String message2, String image2_url, String image3_url);
	
	public void setRead(InfoTableInstance info);
}
