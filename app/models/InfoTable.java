package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="infotable")
public class InfoTable extends Model {

	public String name;
	/**
	 * type of the info message
	 * 
	 */
	public Integer type;
	public String image1_url;

	@JsonIgnore
	@OneToMany(mappedBy="infoTable")
	public List<InfoTable> infoTable;

	public Boolean isRead;


	


}
