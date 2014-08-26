package controllers;

import models.InfoTableInstance;
import play.mvc.Controller;
import service.InfoTableService;
import service.impl.InfoTableServiceImpl;

public class InfoTableController extends Controller{
	
	public static void setRead(Long id) {
		InfoTableInstance info = InfoTableInstance.findById(id);
		InfoTableService infoS = new InfoTableServiceImpl();
		infoS.setRead(info);
	}

}
