package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import dto.DiseaseOccurenceProb;
import dto.DiseaseStatusDto;

import models.Day;
import models.Farmer;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.With;

public class Application extends Controller {

	public static void index() {
	}

	public static void nextday() throws IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		farmer = farmer.gotoNextDay();
		JsonController.farmerJson(farmer);
	}

	public static void observeDiseasesForThisYear() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			renderJSON("");
		}
		List<DiseaseStatusDto> result = new ArrayList<DiseaseStatusDto>();
		HashMap<String, Double> maxValues = new HashMap<String, Double>();
		Double maxProb = 0.0;
		for (int i = 0; i < 365; i++) {
			Day gameDate = farmer.gameDate;
			farmer = farmer.gotoNextDay();
			List<DiseaseOccurenceProb> dopList = DeseasesExpertSystem
					.getDP(farmer);
			for (DiseaseOccurenceProb dop : dopList) {
				if (dop.probability > (farmer.getLuck() * 100)) {
					DiseaseStatusDto status = new DiseaseStatusDto();
					status.diseaseName = dop.name;
					status.status = true;
					status.dateOccurence = gameDate.date;
					status.diseaseProb = dop.probability;
					result.add(status);

				}
				if (maxValues.containsKey(dop.name)) {
					if (maxValues.get(dop.name) < dop.probability) {
						maxValues.put(dop.name, dop.probability);
					}
				} else {
					maxValues.put(dop.name, dop.probability);
				}
			}
		}
		if (result.size() == 0) {
			renderJSON(maxValues);
		}
		renderJSON(result);
	}

}