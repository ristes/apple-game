package controllers;

import dto.StatusDto;
import models.Farmer;
import models.LearnStateEventExecuted;
import models.LearnStateEvents;

public class LearnStateController extends JsonController{
	
	public static void done(Long id) {
		Farmer farmer = AuthController.getFarmer();
		LearnStateEvents event = LearnStateEvents.findById(id);
		LearnStateEventExecuted exec = new LearnStateEventExecuted();
		exec.setEvent(event);
		exec.setDay(farmer.gameDate);
		exec.setFarmer(farmer);
		exec.save();
		toJson(new StatusDto<Void>(true));
	}

}
