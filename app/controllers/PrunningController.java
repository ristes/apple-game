package controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dto.InfoTableInstanceDto;
import dto.StatusDto;
import service.ServiceInjector;
import models.Farmer;
import models.InfoTable;
import models.LogFarmerData;

public class PrunningController extends GameController {

	public static void prune(Double goodPercent)
			throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = checkFarmer();
		LogFarmerData info = ServiceInjector.prunningService.hasPruned(farmer);
		if (info==null) {
			ServiceInjector.prunningService.prune(farmer, goodPercent);
			JsonController.statusJson(farmer);
		} else {
			InfoTableInstanceDto infoT = new InfoTableInstanceDto();
			SimpleDateFormat fd = new SimpleDateFormat("dd:MM");
			infoT.message1 = String.format("Clothing is done on %s with %d successfully prunned branches.", fd.format(info.logdate), info.information);
			JsonController.statusJson(new StatusDto<Void>(true, null, null, farmer, Arrays.asList(infoT)));
		}
	}
	
	public static void checkprunning() throws IOException{
		Farmer farmer = checkFarmer();
		LogFarmerData data = ServiceInjector.prunningService.hasPruned(farmer);
		SimpleDateFormat fd = new SimpleDateFormat("dd:MM");
		if (data!=null) {
			JsonController.statusJson(new StatusDto<Void>(false,String.format("Kroenjeto e zavrseno na den %s so %f uspesno iskroeni grancinja.", fd.format(data.logdate), data.information),null, farmer, null));
		} 
		JsonController.statusJson(new StatusDto<Void>(true));
	}

}
