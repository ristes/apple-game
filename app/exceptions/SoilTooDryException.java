package exceptions;

public class SoilTooDryException extends Exception{
	
	private String message;
	
	public SoilTooDryException() {
		
	}
	
	public SoilTooDryException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
