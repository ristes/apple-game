package service;

import java.util.List;

import dto.OperationBestTimeIntervalDto;
import models.Farmer;
import models.OperationBestTimeInterval;

public interface SoilService {
	
	public List<OperationBestTimeIntervalDto> features(Farmer farmer);

}
