package dto;

import java.util.Date;
import java.util.List;

import models.ExecutedOperation;

public class DiseaseProtectingOperationDto {

	public Long operation_id;
	public Long decease_id;
	public Integer deceaseProtectingFactor;
	public Date startFrom;
	public Date endTo;

	public DiseaseProtectingOperationDto() {
		// TODO Auto-generated constructor stub
	}

	public DiseaseProtectingOperationDto(Long operation_id, Long decease_id,
			int deceaseProtectingFactor, Date startFrom, Date endTo) {
		super();
		this.operation_id = operation_id;
		this.decease_id = decease_id;
		this.deceaseProtectingFactor = deceaseProtectingFactor;
		this.startFrom = startFrom;
		this.endTo = endTo;
	}

	public Boolean isMatched(List<ExecutedOperation> operations) {
		for (ExecutedOperation operation : operations) {
			if (operation_id == operation.operation.id) {
				if (operation.startDate.after(startFrom)) {
					if (operation.startDate.before(endTo)) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
