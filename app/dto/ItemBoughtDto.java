package dto;

import org.hamcrest.core.Is;

import models.ItemInstance;

public class ItemBoughtDto {

	public Long id;

	public Long type_id;

	public String name;

	public String url;

	public Integer count;

	public Double quantity;

	public String store;

	public ItemBoughtDto() {
	}

	public ItemBoughtDto(ItemInstance instance) {
		id = instance.id;
		quantity = instance.quantity;

		type_id = instance.type.id;
		name = instance.type.name;
		url = instance.type.imageurl;
		store = instance.type.store.name;
	}

}
