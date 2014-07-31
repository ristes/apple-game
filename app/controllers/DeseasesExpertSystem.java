package controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Decease;
import models.ExecutedOperation;
import models.Farmer;
import models.OccurredDecease;
import play.db.jpa.GenericModel.JPAQuery;
import play.db.jpa.JPA;
import play.mvc.Controller;
import service.DateService;
import service.DiseaseService;
import service.impl.DateServiceImpl;
import service.impl.DiseaseServiceImpl;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;

public class DeseasesExpertSystem extends Controller {
	
	public static Double coef_of_diminushing = 0.97;

	

	public static void getDeseasePossibility() throws UnknownFunctionException,
			UnparsableExpressionException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			redirect("crafty/login");
		}
		DiseaseService diseaseService = new DiseaseServiceImpl();
		List<DiseaseOccurenceProb> disProbs = diseaseService.getDiseasesProb(farmer);
		renderJSON(disProbs);
	}

	
	
	//get diseases for previous 15 days
	public static void getOccurredDiseases() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null) {
			error("Not logged in");
		}
		DiseaseService diseaseService = new DiseaseServiceImpl();
		renderJSON(diseaseService.getOccurredDiseasesLast15Days(farmer));
	}
	

	
	
}
