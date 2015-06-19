package dto;

import models.Item;

public class StoreItemDto {

	public Long id;
	public String store;
	public String name;
	public String description;
	public Double price;
	public String url;
	public Double size;
	public Boolean perHa;
	public String metadata;

	public StoreItemDto() {

	}

	public StoreItemDto(Item item) {
		this.id = item.id;
		this.name = item.name;
		this.description = item.description;
		this.url = item.imageurl;
		this.price = (double) item.price;
		this.store = item.store.name;
		this.perHa = item.perHa;
		this.metadata = item.metadata;
		
	}
}
