package dao;

import java.util.List;

import models.Base;
import models.Field;
import models.Seedling;
import models.Decease;
import models.DeceaseImpact;
import models.ExecutedOperation;

public interface DeceasesDao {

	public List<ExecutedOperation> executedValidOperations(Decease decease,
			Field field);

	public DeceaseImpact getDeceaseThreshold(Decease decease,
			Seedling seadlings, Base base);

}
