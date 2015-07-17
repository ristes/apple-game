package models;

import java.util.List;

import javax.persistence.Column;
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
	
	@Column(name="image_url")
	public String imageUrl;
	
	/**
	 * 1 -without image
	 * 2 - with image
	 */
	@Column(name="type_question")
	public Integer typeQuestion;
	
	public String name;
	
	@Column(name="more_info")
	public String moreInfo;
	
	public String toString() {
		return name;	
	}
	

}
