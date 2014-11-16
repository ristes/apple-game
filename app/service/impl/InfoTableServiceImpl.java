package service.impl;

import java.util.ArrayList;
import java.util.List;

import models.Farmer;
import models.InfoTable;
import models.InfoTableInstance;
import service.InfoTableService;
import dto.InfoTableInstanceDto;

public class InfoTableServiceImpl implements InfoTableService{

	
	/**
	 * type 1 message: info about some staff ex. ,,You lost 200kg apples"
	 * 
	 * @param image1_url
	 * @param message
	 */
	@Override
	public void createT1(Farmer farmer, String message1, String image2_url) {
		InfoTable type = InfoTable.findById(3l);
		InfoTableInstance infoIns = new InfoTableInstance();
		infoIns.isRead = false;
		infoIns.message1 = message1;
		infoIns.infoTable = type;
		infoIns.plantation = farmer.field.plantation;
		infoIns.image2_url = image2_url;
		infoIns.save();
		
	}

	
	/**
	 * type 2 message: info with two actions happened in same time like 
	 * ,,You lost 200kg apples from heavy rain but received 2000 den from refunding"
	 * 
	 * @param image1_url
	 * @param message
	 * @param lost_apples
	 */
	@Override
	public void createT2(Farmer farmer, String message1, String message2, String image2_url,
			String image3_url) {
		InfoTable type = InfoTable.findById(4l);
		InfoTableInstance infoIns = new InfoTableInstance();
		infoIns.isRead = false;
		infoIns.infoTable = type;
		infoIns.image2_url = image2_url;
		infoIns.image3_url = image3_url;
		infoIns.message1 = message1;
		infoIns.message2 = message2;
		infoIns.plantation = farmer.field.plantation;
		infoIns.save();
	}


	@Override
	public void setRead(InfoTableInstance info) {
		info.isRead = true;
		info.save();
		
	}


	@Override
	public List<InfoTableInstanceDto> news(Farmer farmer) {
		if (farmer==null) {
			return new ArrayList<InfoTableInstanceDto>();
		}
		List<InfoTableInstanceDto> newsDto = new ArrayList<InfoTableInstanceDto>();
		List<InfoTableInstance> news = InfoTableInstance.find("byPlantationAndIsRead",farmer.field.plantation,false).fetch();
		for (InfoTableInstance ins : news) {
			ins.isRead = true;
			ins.save();
			newsDto.add(new InfoTableInstanceDto(ins));
		}
		return newsDto;
	}
	
	
	

}
