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
	
	public static int sumOfQuantity(List<FertilizerOperationDto> list) {
		int sum = 0;
		for (FertilizerOperationDto fo:list) {
			sum+=fo.quantity;
		}
		return sum;
	}
}
