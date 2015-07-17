package dto;

import java.util.List;

public class QuestionnaireDto {
	
	public Long id;
	public Integer ordnum;
	public String name;
	public String imageUrl;
	public Integer type;
	public String moreInfo;
	
	public List<AnswerDto> answers;
	

}
