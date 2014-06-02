package controllers;

import java.util.ArrayList;
import java.util.List;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseOccurenceProb;

import models.Decease;
import models.ExecutedOperation;
import models.Farmer;
import models.GameContext;

import play.mvc.Controller;

public class DeseasesExpertSystem extends Controller {
	


	public static void getUserLuck() {
		Farmer f = AuthController.getFarmer();
		if (f != null) {
			f.generateLuck();
			Double luck = f.getLuck();
			renderJSON(luck);
		}
		renderJSON("null");
	}
	
	public static List<DiseaseOccurenceProb> getDP(Farmer farmer) {
		List<DiseaseOccurenceProb> disProbs = new ArrayList<DiseaseOccurenceProb>();
		List<Decease> deceases = Decease.findAll();
		for (Decease disease : deceases) {
			DiseaseOccurenceProb dis = new DiseaseOccurenceProb();
			dis.name = disease.name;
			Double prob = disease.getRisk(farmer);
			Double diminishingFactor = disease.getOperationsDiminushing(farmer);
			dis.probability = prob-prob*diminishingFactor;
			disProbs.add(dis);
		}
		return disProbs;
	}
	
	public static void getDeseasePossibility() throws UnknownFunctionException, UnparsableExpressionException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			redirect("crafty/login");
		}
		List<DiseaseOccurenceProb> disProbs = getDP(farmer);
		renderJSON(disProbs);
	}
	
	public static void getOperations(Farmer farmer) {
		
	}
	
	

	public static List<ExecutedOperation> getProtections(Farmer f) {
		List<ExecutedOperation> exOperations = f.field.executedOperations;
		return null;
	}
}
