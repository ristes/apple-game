package exceptions;

public class NotSuchItemException extends Exception{
	
	public String message;
	
	public NotSuchItemException() {
		// TODO Auto-generated constructor stub
	}
	
	public NotSuchItemException(String message) {
		this.message = message;
	}

}
