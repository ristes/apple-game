package exceptions;

public class UnauthorizedException extends Exception{
	
	public String message;
	
	public UnauthorizedException() {
		// TODO Auto-generated constructor stub
	}
	
	public UnauthorizedException(String message) {
		this.message = message;
	}

}
