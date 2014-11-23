package service;

import java.util.Date;
import java.util.List;

import models.Farmer;
import models.Item;
import models.OperationBestTimeInterval;
import exceptions.NotEnoughMoneyException;
import exceptions.NotSuchItemException;

public interface FertilizeService {

	public Farmer fertilize(Farmer farmer, Double n, Double p, Double k, Double ca, Double b, Double mg) throws NotEnoughMoneyException;
	public int finalEvaluationFertilizer(Farmer farmer);
	public Farmer finalEvaluation(String name, Farmer farmer);
	public Double finalEvaluationItem(Farmer farmer, Item item);
	public Double evaluateFertilizer(Farmer farmer, Item item);
	public List<OperationBestTimeInterval> intervalsForFertilizer(Item item);
	public Boolean isInInterval(List<OperationBestTimeInterval> intervalsForPrevetions,  Date date);
	public Boolean checkNeedOfN(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfP(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfK(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfCa(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfB(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfZn(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfMg(Farmer farmer) throws NotSuchItemException;
	public Boolean checkNeedOfFertilizerType(Farmer farmer, Item item);
}
