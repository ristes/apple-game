package dao.impl;

import java.util.List;

import models.Base;
import models.Field;
import models.Seedling;
import models.Decease;
import models.DeceaseImpact;
import models.ExecutedOperation;
import dao.DeceasesDao;

public class DeceasesDaoImpl implements DeceasesDao {

	public List<ExecutedOperation> executedValidOperations(Decease decease,
			Field field) {
		// TODO Auto-generated method stub
		return null;
	}

	public DeceaseImpact getDeceaseThreshold(Decease decease,
			Seedling seadlings, Base base) {

		return DeceaseImpact.find(
				"type.id=?1 and base.id=?2 and decease.id=?3",
				seadlings.type.id, base.id, decease.id).first();
	}

}
