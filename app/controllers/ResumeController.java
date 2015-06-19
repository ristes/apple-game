package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Farmer;
import service.ServiceInjector;
import dto.ResumeMessageDto;
import dto.ResumeResult;

public class ResumeController extends GameController{
	
	public static void resume() {
		Farmer farmer = checkFarmer();
		Integer recolteYear = ServiceInjector.dateService.recolteYear(farmer.gameDate.date)-1;
		ResumeMessageDto<List<String>> moneySpent = ServiceInjector.resumeService.moneySpent(farmer, recolteYear);
		ResumeMessageDto<List<String>> moneyEarned = ServiceInjector.resumeService.moneyEarned(farmer, recolteYear);
		ResumeMessageDto<Map<String,Integer>> operationsExecuted = ServiceInjector.resumeService.operationsExecuted(farmer, recolteYear);
		ResumeMessageDto<Map<String,Integer>> diseasesOccurred = ServiceInjector.resumeService.diseasesOccured(farmer, recolteYear);
		ResumeMessageDto<List<String>> applesSold = ServiceInjector.resumeService.applesSold(farmer, recolteYear);
		ResumeMessageDto<List<String>> applesBurned = ServiceInjector.resumeService.applesBurned(farmer, recolteYear);
		ResumeResult resume = new ResumeResult();
		resume.intTypes.put("moneySpent",moneySpent);
		resume.intTypes.put("moneyEarned",moneyEarned);
		resume.hashTypes.put("operationsExecuted",operationsExecuted);
		resume.hashTypes.put("diseasesOccurred",diseasesOccurred);
		resume.intTypes.put("applesSold",applesSold);
		resume.intTypes.put("applesBurned", applesBurned);

		renderJSON(resume);
		
		
	}
	
	public static void seenResume() {
		Farmer farmer = checkFarmer();
		farmer.isNewSeason = false;
		farmer.save();
	}

}
