package dto;

import java.text.SimpleDateFormat;

import models.OperationBestTimeInterval;

public class OperationBestTimeIntervalDto {
	
	private String name;
	private String startFrom;
	private String endTo;
	private Double quantity;
	
	public OperationBestTimeIntervalDto(OperationBestTimeInterval obti) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM");
		this.name = obti.fertilizationBestTime.fertilizer.name;
		this.startFrom = sdf.format(obti.startFrom);
		this.endTo = sdf.format(obti.endTo);
		this.quantity = obti.quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartFrom() {
		return startFrom;
	}

	public void setStartFrom(String startFrom) {
		this.startFrom = startFrom;
	}

	public String getEndTo() {
		return endTo;
	}

	public void setEndTo(String endTo) {
		this.endTo = endTo;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}
	

}
