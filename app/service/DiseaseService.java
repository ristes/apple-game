package service;

import java.util.List;

import models.Disease;
import models.ExecutedOperation;
import models.Farmer;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;

public interface DiseaseService {

	public List<DiseaseOccurenceProb> getDiseasesProb(Farmer farmer);
	public int diseases(Farmer farmer);
//	public List<String> getOccurredDiseasesLast15Days(Farmer farmer);
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer);
	public List<Disease> getOccurredDiseasesEntitiesLast15Days(Farmer farmer);
	public List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Disease disease);
	public List<ExecutedOperation> getProtections(Farmer f);
}
