package controllers;

import java.util.List;

import models.Disease;
import models.Farmer;
import play.mvc.Controller;
import service.DiseaseService;
import service.MoneyTransactionService;
import service.ServiceInjector;
import service.impl.DiseaseServiceImpl;
import service.impl.TransactionServiceImpl;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseOccurenceProb;
import exceptions.NotEnoughMoneyException;

public class DeseasesExpertSystem extends GameController {

	public static Double coef_of_diminushing = 0.97;

	public static void getDeseasePossibility() throws UnknownFunctionException,
			UnparsableExpressionException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			redirect("crafty/login");
		}
		DiseaseService diseaseService = new DiseaseServiceImpl();
		List<DiseaseOccurenceProb> disProbs = diseaseService
				.getDiseasesProb(farmer);
		renderJSON(disProbs);
	}

	// get diseases for previous 15 days
	public static void getOccurredDiseases() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		renderJSON(ServiceInjector.diseaseService.getOccurredDiseasesLast15Days(farmer));
	}

	public static class DiseaseHint {
		public boolean status;
		public Farmer farmer;
		public String hint;
		public String problem;
	}

	public static void buyAdvice(String name) throws Exception {
		final Farmer _farmer = checkFarmer();
		final Disease disease = Disease.find("name", name).first();
		DiseaseHint hint = new DiseaseHint();
		hint.status = false;
		if (disease != null) {
			MoneyTransactionService transactionService = new TransactionServiceImpl();
			transactionService.commitMoneyTransaction(_farmer,
					disease.hintPrice);
			hint.status = true;
			hint.farmer = _farmer;
			hint.hint = disease.hint;
		} else {
			hint.problem = "diseases.invalid_disease";
		}
		String res = JsonController.toJsonString(hint, "field", "gameDate",
				"weatherType", "plantation");
		renderJSON(res);
	}

}
