package exceptions;

public class NotEnoughMoneyException extends Exception{
	
	private String message;
	
	private Integer price;
	
	public NotEnoughMoneyException() {
		
	}
	
	public NotEnoughMoneyException(String message) {
		this.message = message;
	}
	
	public NotEnoughMoneyException(String message, Integer price) {
		this.message = message;
		this.price = price;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Integer getPrice() {
		return price;
	}

}
