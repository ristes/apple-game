package dao;

import models.Day;
import models.Farmer;

public class FarmerTransactionDao {
	
	public String username;
	
	public int balans;
	
	public Boolean status;
	
	public Day gameDate;
	
	public String currentState;
	
	
	public FarmerTransactionDao(Farmer farmer, Boolean status) {
		this.status = status;
		this.username = farmer.username;
		this.balans = farmer.balans;
		this.gameDate = farmer.gameDate;
		this.currentState = farmer.currentState;
	}
	
	public FarmerTransactionDao(Boolean status) {
		this.status = status;
	}

}
