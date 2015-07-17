package service;

import java.util.List;

import dto.QuestionnaireDto;
import dto.QuizResultsDto;
import models.Answer;
import models.Disease;
import models.Farmer;
import models.Item;
import models.Questionnaire;

public interface QuizService {
	
	public static Integer MAX_NUM_QUESTIONS = 5;
	
	public List<Questionnaire> questionsByDiseases(List<Disease> diseases);
	public List<Questionnaire> questionsByItems(List<Item> items);
	public List<Questionnaire> questionsByFarmer(Farmer farmer);
	public void answerQuestion(Farmer farmer, Answer answer);
	public void order(List<QuestionnaireDto> result);
	public QuizResultsDto submitAnswers(Farmer farmer, int correct, int wrong);
	public QuizResultsDto isQuizAnsweredThisYear(Farmer farmer);

}
