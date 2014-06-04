package exceptions;

public class NotEnoughMoneyException extends Exception{
	
	private String message;
	
	public NotEnoughMoneyException() {
		
	}
	
	public NotEnoughMoneyException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
