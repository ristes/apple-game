package dto;

import models.Day;
import models.Farmer;

public class FarmerTransactionDto {
	
	public String username;
	
	public double balans;
	
	public Boolean status;
	
	public Day gameDate;
	
	public String currentState;
	
	
	public FarmerTransactionDto(Farmer farmer, Boolean status) {
		this.status = status;
		this.username = farmer.username;
		this.balans = farmer.getBalance();
		this.gameDate = farmer.gameDate;
		this.currentState = farmer.currentState;
	}
	
	public FarmerTransactionDto(Boolean status) {
		this.status = status;
	}

}
