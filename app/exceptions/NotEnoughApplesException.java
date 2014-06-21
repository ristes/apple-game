package exceptions;

public class NotEnoughApplesException extends Exception{

	public String message;
	
	public NotEnoughApplesException() {
		
	}
	
	public NotEnoughApplesException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
