package exceptions;

public class NotAllowedException extends Exception{
	
	public String message;
	
	public NotAllowedException() {
		// TODO Auto-generated constructor stub
	}
	
	public NotAllowedException(String message) {
		this.message = message;
	}

}
