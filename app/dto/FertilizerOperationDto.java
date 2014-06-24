package dto;

import java.util.Date;
import java.util.List;

public class FertilizerOperationDto {

	public Long id;
	public Long fertilizer_id;
	public Long operation_id;
	public Date startFrom;
	public Date endTo;
	public Double quantity;

	public static double sumOfQuantity(List<FertilizerOperationDto> list) {
		double sum = 0;
		for (FertilizerOperationDto fo : list) {
			sum += fo.quantity;
		}
		return sum;
	}
}
