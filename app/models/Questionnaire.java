package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="questionnaire_game")
public class Questionnaire extends Model{
	
	
	@ManyToOne
	@JsonIgnore
	public Disease disease;
	
	
	@ManyToOne
	@JsonIgnore
	public Item item;
	
	@OneToMany(mappedBy="question")
	public List<Answer> answers;
	
	public String name;
	
	public String toString() {
		return name;	
	}
	

}