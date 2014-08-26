package dao;

import java.util.List;

import models.Farmer;
import models.Item;
import dto.FertilizerOperationDto;

public interface FertilizingDao {

	public List<FertilizerOperationDto> getFertilizationOper(
			Farmer farmer, Item item);
	public List<FertilizerOperationDto> getExecFertOper(Farmer farmer,
			Long operation_id);
}
