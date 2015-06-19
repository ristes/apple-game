package controllers;

import service.ServiceInjector;

public class RankingController extends JsonController{
	
	public static void rank(Integer year) {
		toJson(ServiceInjector.rankingService.rank(year),"field","password");
	}

}
