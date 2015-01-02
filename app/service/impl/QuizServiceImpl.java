package service.impl;

import java.util.ArrayList;
import java.util.List;

import dto.QuestionnaireDto;
import models.Answer;
import models.Disease;
import models.Farmer;
import models.Item;
import models.Questionnaire;
import service.QuizService;
import service.ServiceInjector;


public class QuizServiceImpl implements QuizService{

	@Override
	public List<Questionnaire> questionsByDiseases(List<Disease> diseases) {
		List<Questionnaire> questions = new ArrayList<Questionnaire>();
		for (Disease disease:diseases) {
			List<Questionnaire> qS = Questionnaire.find("byDisease",disease).fetch();
			questions.addAll(qS);
		}
		return questions;
		
	}

	@Override
	public List<Questionnaire> questionsByItems(List<Item> items) {
		List<Questionnaire> questions = new ArrayList<Questionnaire>();
		for (Item item:items) {
			List<Questionnaire> qS = Questionnaire.find("byItem",item).fetch();
			questions.addAll(qS);
		}
		return questions;
		
	}

	@Override
	public List<Questionnaire> questionsByFarmer(Farmer farmer) {
		List<Questionnaire> qS = new ArrayList<Questionnaire>();
		List<Disease> diseases = ServiceInjector.diseaseService.getOccurredDiseasesEntitiesLast15Days(farmer);
		qS.addAll(questionsByDiseases(diseases));
		List<Item> items = ServiceInjector.fertilizeService.checkTheRequiredItemsFromContext(farmer);
		qS.addAll(questionsByItems(items));
		return qS;
	}

	@Override
	public void answerQuestion(Farmer farmer, Answer answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void order(List<QuestionnaireDto> result) {
		int ordnum = 1;
		for (QuestionnaireDto qDto:result) {
			qDto.ordnum= ordnum;
			ordnum++;
		}
		
	}

}
