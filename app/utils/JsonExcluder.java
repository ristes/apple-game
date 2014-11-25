package utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonExcluder {
	
	public static String byField(String json, String field) {
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(json);
		return el.getAsJsonObject().get(field).getAsString();
	}

}
