package jobs;

import java.util.List;

import models.Farmer;
import play.jobs.Job;
import play.jobs.On;
import service.FarmerService;
import service.impl.FarmerServiceImpl;

@On("0 0 4 * * ?")
public class NextDayJobs extends Job{
	
	@Override
	public void doJob() throws Exception {
		List<Farmer> farmers = Farmer.find("is_active=true").fetch();
		FarmerService farmerService = new FarmerServiceImpl();
		for (Farmer farmer: farmers) {
			farmerService.gotoNextDay(farmer);
		}
	}

}
