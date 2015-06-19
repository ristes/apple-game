package service;

import java.util.List;

import models.Farmer;
import models.LearnStateEvents;

public interface LearnStateEventService {
	
	public LearnStateEvents generate(Farmer farmer);
	public List<LearnStateEvents> avail(Farmer farmer);

}
