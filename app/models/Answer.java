package models;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="answer_question_game")
public class Answer extends Model{
	
	@ManyToOne
	public Questionnaire question;
	
	public String name;
	
	public Boolean is_correct;
	
	public String imageUrl;
	
	public String toString() {
		return name;
	}

}
