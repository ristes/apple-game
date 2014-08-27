package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Farmer;
import models.InfoTableInstance;
import play.mvc.Controller;
import service.InfoTableService;
import service.impl.InfoTableServiceImpl;

public class InfoTableController extends Controller{
	
	public static void news() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("login");
		}
		InfoTableService itService = new InfoTableServiceImpl();
		JsonController.toJson(itService.news(farmer),"infoTable");
	}
	
	public static void setRead(Long id) {
		InfoTableInstance info = InfoTableInstance.findById(id);
		InfoTableService infoS = new InfoTableServiceImpl();
		infoS.setRead(info);
	}

}
