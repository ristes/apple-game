package service;

import java.util.List;

import models.Disease;
import models.ExecutedOperation;
import models.Farmer;
import dto.DiseaseOccurenceProb;
import dto.DiseaseProtectingOperationDto;

public interface DiseaseService {
	
	public static final Integer DISEASE_CHECK_PERIOD = 5;
	
	public static final int TYPE_DISEASE = 1;
	public static final int TYPE_PEST = 2;
	public static final int TYPE_NATURAL_DISASTER = 3;
	
	public static final String NAME_NATURAL_DISEASE_ICE = "IceDemage";
	public static final String NAME_NATURAL_DISEASE_FREEZING = "FreezingDemage";
	public static final String NAME_NATURAL_DISEASE_UV_DEMAGE = "UVDemage";

	public void evaluateDiseases(Farmer farmer);
	public List<DiseaseOccurenceProb> getDiseasesProb(Farmer farmer);
	public int diseases(Farmer farmer);
	public List<String> getOccurredDiseasesLast15Days(Farmer farmer);
	public List<Disease> getOccurredDiseasesEntitiesLast15Days(Farmer farmer);
	public List<DiseaseProtectingOperationDto> getMmax(Farmer farmer,
			Disease disease);
	public List<ExecutedOperation> getProtections(Farmer f);
	
	public Double getRefund(Farmer context, Disease disease);
	public Double getRisk(Farmer context, Disease disease);
	public Double addBaseProb(Farmer farmer, Disease disease, Double prob);
	public int getOperationsDiminushingFactor(Farmer context, Disease disease);
	public Double getDemage(Farmer farmer, Disease disease);
	public String getImageUrl(Disease disease);
	
}
