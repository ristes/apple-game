package models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import play.data.validation.Range;
import play.db.jpa.Model;

/**
 * The definition for the items
 * 
 * @author ristes
 * 
 */
@Entity
@Table(name = "item")
public class Item extends Model {

	/**
	 * The name of the item
	 */
	public String name;

	/**
	 * The item description
	 */
	public String description;

	/**
	 * The price for the item
	 */
	public int price;

	/**
	 * Tells if the price is per Ha
	 */
	public Boolean perHa;
	
	/**
	 * Tells if the item is visible as an item in the sliding menu
	 */
	public Boolean visibleInBoughtItems;

	/**
	 * Where can we buy the item
	 */
	@ManyToOne
	public Store store;

	/**
	 * JSON object that describes the object
	 */
	public String metadata;

	/**
	 * The image that will be displayed for the item
	 */
	public String imageurl;

	/**
	 * How this item affects the ecology. Ranges from 0 to 10, where 0 is the
	 * best, with no pollution at all, and 10 is high pollution
	 */
	@Range(min = 0, max = 10)
	public int pollutionCoefficient;

	/**
	 * When the item will be shown in the store
	 */
	public int activationRecolteYear;
	
	public Boolean isValid;

	/**
	 * How long the item will be active. If -1, it lasts forever.
	 */
	public int expirationInYears;
	
	public Integer expirationInMonths;

	@OneToMany(mappedBy = "fertilizer")
	public List<FertilizationOperation> fertilizationOperations;

	/**
	 * The operation which is executed with this item
	 */
	@ManyToOne
	public Operation operation;

	public String toString() {
		return name;
	}

	@JsonIgnore
	@Transient
	public String storeName;

	public String suggestToBuy() {
		JsonParser parser = new JsonParser();
		if (metadata != null || (metadata != null && !metadata.equals(""))) {
			JsonObject el = parser.parse(metadata).getAsJsonObject();
			String suggest_to_buy = null;
			if (el.has("suggest-to-buy")) {
				suggest_to_buy = el.get("suggest-to-buy").getAsString();
			}
			return suggest_to_buy;
		}
		return null;
	}

}
