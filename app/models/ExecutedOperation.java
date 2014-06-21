package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

/**
 * Executed operation on some field
 * 
 * @author ristes
 * 
 */
@Entity
public class ExecutedOperation extends Model {

	/**
	 * On which field the operation is executed
	 */
	
	@ManyToOne
	public Field field;

	/**
	 * Which operation is executed
	 */
	@ManyToOne
	public Operation operation;
	
	@OneToOne
	public ItemInstance itemInstance;

	/**
	 * When is it executed
	 */
	public Date startDate;
	
	public ExecutedOperation clone() {
		ExecutedOperation result = new ExecutedOperation();
		result.field = field;
		result.operation = operation;
		result.startDate = (Date)startDate.clone();
		return result;
	}

}
