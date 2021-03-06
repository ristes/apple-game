package service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.Answer;
import models.Disease;
import models.Farmer;
import models.Item;
import models.LogFarmerData;
import models.Questionnaire;
import service.LogFarmerDataService;
import service.QuizService;
import service.ServiceInjector;

import com.google.gson.Gson;

import dto.QuestionnaireDto;
import dto.QuizResultsDto;


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
	
	public List<Questionnaire> randomQuestions(int num) {
		List<Questionnaire> result = new ArrayList<Questionnaire>();
		Set<Long> diffNums = new HashSet<Long>();
		long questions = Questionnaire.count();
		while(diffNums.size()!=num) {
			Long randomNum = (long)(Math.random()*questions+1);
			diffNums.add(randomNum);
		}
		for (Long random:diffNums) {
			result.add((Questionnaire)Questionnaire.findById(random));
		}
		return result;
	}

	@Override
	public List<Questionnaire> questionsByFarmer(Farmer farmer) {
		
//		List<Questionnaire> qS = new ArrayList<Questionnaire>();
//		List<Disease> diseases = ServiceInjector.diseaseService.getOccurredDiseasesEntitiesLast15Days(farmer);
//		qS.addAll(questionsByDiseases(diseases));
//		List<Item> items = ServiceInjector.fertilizeService.checkTheRequiredItemsFromContext(farmer);
//		qS.addAll(questionsByItems(items));
		List<Questionnaire> res = randomQuestions(5);
		return res;
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

	@Override
	public QuizResultsDto submitAnswers(Farmer farmer, int correct, int wrong) {
		Integer plusQuantity = (farmer.productQuantity / 10) * correct/(wrong+correct);
		Integer plusEco = 10 * correct/(wrong+correct);
		farmer.productQuantity += plusQuantity;
		ServiceInjector.ecoPointsService.add(farmer, plusEco);
		farmer.save();
		QuizResultsDto results = new QuizResultsDto();
		results.hits = correct;
		results.misses = wrong;
		results.apples_gained = plusQuantity;
		results.eco_gained = plusEco;
		return results;
		
	}

	@Override
	public QuizResultsDto isQuizAnsweredThisYear(Farmer farmer) {
		LogFarmerData data = LogFarmerData.find("byTypelogAndRecolteYearAndFarmer",LogFarmerDataService.QUIZ_ANSWERED,ServiceInjector.dateService.recolteYear(farmer.gameDate.date), farmer).first();
		if (data==null) {
			return null;
		}
		QuizResultsDto result = new Gson().fromJson(data.jsonDataInfo, QuizResultsDto.class);
		return result;
	}

}
