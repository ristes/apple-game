package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import models.Answer;
import models.Farmer;
import models.Questionnaire;
import service.ServiceInjector;
import dto.AnswerDto;
import dto.QuestionnaireDto;

public class QuestionnaireController extends GameController{

	
	public static void quiz() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null)  {
			renderJSON("");
		}
		List<Questionnaire> questions = ServiceInjector.quizService.questionsByFarmer(farmer);
		List<QuestionnaireDto> result = new ArrayList<QuestionnaireDto>();
		for (Questionnaire q: questions) {
			QuestionnaireDto qdto = new QuestionnaireDto();
			qdto.id = q.id;
			qdto.name = q.name;
			qdto.answers = new ArrayList<AnswerDto>();
			for (Answer a: q.answers) {
				AnswerDto ans = new AnswerDto();
				ans.id = a.id;
				ans.name = a.name;
				ans.imageUrl = a.imageUrl;
				qdto.answers.add(ans);
			}
			result.add(qdto);
		}
		Collections.shuffle(result);
		ServiceInjector.quizService.order(result);
		if (result.size()>ServiceInjector.quizService.MAX_NUM_QUESTIONS) {
			result = result.subList(0, ServiceInjector.quizService.MAX_NUM_QUESTIONS);
		}
		renderJSON(result);
	}
}
