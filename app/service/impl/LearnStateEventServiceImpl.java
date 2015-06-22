package service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.annotations.Sort;

import play.db.jpa.JPA;
import dto.DiseaseProtectingOperationDto;
import models.Farmer;
import models.LearnStateEventExecuted;
import models.LearnStateEvents;
import service.FarmerService;
import service.LearnStateEventService;
import service.ServiceInjector;

public class LearnStateEventServiceImpl implements LearnStateEventService {

	@Override
	public LearnStateEvents generate(Farmer farmer) {
		if (farmer.subState.equals(FarmerService.SUBSTATE_TEST_PERIOD)) {
			List<LearnStateEvents> res = avail(farmer);
			if (res.size() > 0) {
				Collections.sort(res, Collections.reverseOrder());
				return res.get(0);
			}
		}
		return null;
	}

	@Override
	public List<LearnStateEvents> avail(Farmer farmer) {

		List<LearnStateEvents> avail = new ArrayList<LearnStateEvents>();
		String sqlString = "SELECT * FROM learn_state_events WHERE id NOT IN (SELECT learn_state_events.id FROM learn_state_events inner join learn_state_event_executed on learn_state_events.id=learn_state_event_executed.event_id where farmer_id=:id)";
		Query query = JPA.em().createNativeQuery(sqlString);
		query.setParameter("id", farmer.id);
		List<Object[]> objRes = query.getResultList();
		for (Object[] obj : objRes) {
			Long id = ((BigInteger) obj[0]).longValue();
			Date date = ServiceInjector.dateService.convertDateTo70(farmer.gameDate.date);
			LearnStateEvents event = LearnStateEvents.find(
					"id=?1 and dateStart<=?2 and dateEnd>=?3", id,
					date, date).first();
			if (event != null) {
				avail.add(event);
			}

		}

		Collections.sort(avail, Collections.reverseOrder());
		return avail;
	}

}
