package dao;

import java.util.ArrayList;
import java.util.List;

import dto.FridgeShelf;

public class FridgeUsageDto {

	public int fridgeType;

	public String fridgeName;
	
	public Double price;

	public List<FridgeShelf> shelfs;

	public int used;

	public int capacity;
	
	public int activeFrom;

	public FridgeUsageDto() {
		shelfs = new ArrayList<FridgeShelf>();
	}
}
