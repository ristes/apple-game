package service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import play.db.jpa.JPA;
import dto.DiseaseProtectingOperationDto;
import models.Farmer;
import models.LearnStateEventExecuted;
import models.LearnStateEvents;
import service.LearnStateEventService;

public class LearnStateEventServiceImpl implements LearnStateEventService{

	@Override
	public LearnStateEvents generate(Farmer farmer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LearnStateEvents> avail(Farmer farmer) {
		
		List<LearnStateEvents> avail = new ArrayList<LearnStateEvents>();
		String sqlString = "SELECT * FROM learn_state_events WHERE id NOT IN (SELECT learn_state_events.id FROM applegame.learn_state_events inner join learn_state_event_executed on learn_state_events.id=learn_state_event_executed.event_id where farmer_id=:id)";
		Query query = JPA.em().createNativeQuery(sqlString);
		query.setParameter("id", farmer.id);
		List<Object[]> objRes = query.getResultList();
		for (Object[] obj:objRes) {
			Long id = ((BigInteger)obj[0]).longValue();
			avail.add((LearnStateEvents) LearnStateEvents.findById(id));
		}
		
		
		return avail;
	}

}
