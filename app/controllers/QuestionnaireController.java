package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.Answer;
import models.Farmer;
import models.Questionnaire;
import service.QuizService;
import service.ServiceInjector;
import dto.AnswerDto;
import dto.FarmerAnswerDto;
import dto.QuestionnaireDto;
import dto.QuizResultsDto;
import dto.StatusDto;

public class QuestionnaireController extends GameController{

	
	public static void submit(Integer correct, Integer wrong) throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null)  {
			renderJSON("");
		}
		if (correct + wrong > QuizService.MAX_NUM_QUESTIONS) {
			renderJSON(new StatusDto(false));
		}
		QuizResultsDto result = ServiceInjector.quizService.submitAnswers(farmer, correct, wrong);
		ServiceInjector.logFarmerDataService.logQuizAnswered(farmer, result);
		StatusDto<QuizResultsDto> status = new StatusDto<QuizResultsDto>(true,null,null,farmer,result, null);
		
		JsonController.statusJson(status);
//		FarmerAnswerDto aDto = new Gson().fromJson(body, FarmerAnswerDto.class);
		
	}
	
	public static void quiz() throws JsonGenerationException, JsonMappingException, IOException {
		Farmer farmer = AuthController.getFarmer();
		if (farmer==null)  {
			renderJSON("");
		}
		QuizResultsDto qDto = ServiceInjector.quizService.isQuizAnsweredThisYear(farmer);
		if (qDto!=null) {
			StatusDto statusDto = new StatusDto(false, null, null, farmer, qDto, null);
			JsonController.statusJson(statusDto);
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
				ans.is_correct = a.is_correct;
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
