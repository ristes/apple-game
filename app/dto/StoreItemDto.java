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

	public StoreItemDto() {

	}

	public StoreItemDto(Item item) {
		id = item.id;
		name = item.name;
		description = item.description;
		url = item.imageurl;
		price = (double) item.price;
		store = item.store.name;
	}
}
