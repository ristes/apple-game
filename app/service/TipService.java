package service;

import java.util.List;

import models.Farmer;

public interface TipService {
	
	public String randomTip(List<String> tips);
	
	public List<String> tipgenerator(Farmer farmer) ;

}
