package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import dto.DiseaseProtectingOperationDto;
import play.db.jpa.JPA;
import models.Base;
import models.Farmer;
import models.Field;
import models.Seedling;
import models.Disease;
import models.DeceaseImpact;
import models.ExecutedOperation;

public interface DeceasesDao {

	public List<ExecutedOperation> executedValidOperations(Disease decease,
			Field field);

	public DeceaseImpact getDeceaseThreshold(Disease decease,
			Seedling seadlings, Base base);
	
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer);
	public List<Disease> getOccurredDiseasesEntitiesLast15Days(Farmer farmer);
	public List<DiseaseProtectingOperationDto> getDiseaseProtectingOpersShouldBeDoneToDate(Disease disease, Date curDate);

}
