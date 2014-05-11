package controllers;

import java.util.Date;
import java.util.List;

import models.GameContext;
import models.Plantation;
import models.Decease;
import models.DeceaseImpact;
import models.ExecutedOperation;
import play.mvc.Controller;
import dao.DeceasesDao;

public class Deceases extends Controller {
	

	static DeceasesDao deceases;

	public static void shouldDeceaseOccure(Decease decease,
			Plantation plantation, Date date) {

		List<ExecutedOperation> executedValidOperations = deceases
				.executedValidOperations(decease, plantation.field);

		DeceaseImpact deceaseThreshold = deceases.getDeceaseThreshold(
				decease, plantation.seadlings, plantation.base);

		int treshold = deceaseThreshold.threshold;

		// modify the threshold using the preventing operations

		// generate the probability from the conditions

	}
	
}
