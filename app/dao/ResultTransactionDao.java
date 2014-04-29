package dao;

public class ResultTransactionDao {
	
	public Boolean status;
	
	public Long value;
	
	public ResultTransactionDao(Boolean status) {
		this.status = status;
	}
	
	public ResultTransactionDao(Boolean status, Long value) {
		this.status = status;
		this.value = value;
	}

}
