package exceptions;

public class TooWaterOnFieldException extends Exception{

	public String message;
	
	public TooWaterOnFieldException() {
		
	}
	
	public TooWaterOnFieldException(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
