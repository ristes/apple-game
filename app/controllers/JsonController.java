package controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.persistence.OneToMany;

import models.Farmer;
import json.AnnotationAndIngoreFieldsFilter;
import json.CollaseAnnotationAndExpandFieldsFilter;
import play.mvc.Controller;
import play.mvc.Http;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public abstract class JsonController extends Controller {

	@JsonFilter("filter properties by name")
	class PropertyFilterMixIn {
	}

	
	protected static void farmerJson(Farmer farmer) throws JsonGenerationException, JsonMappingException, IOException {
		toJson(farmer, "field", "gameDate", "weatherType","plantation");
	}
	
	protected static void toJson(Object o, String... expandFields)
			throws JsonGenerationException, JsonMappingException, IOException {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);

		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
		mapper.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);

		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("filter properties by name", new OneToManyFilter(
						expandFields));

		ObjectWriter jsonWriter = mapper.writer(filters);

		jsonWriter.writeValue(response.out, o);
		renderJSON("");

	}

	static class OneToManyFilter extends CollaseAnnotationAndExpandFieldsFilter {

		public OneToManyFilter(String... propertyArray) {
			super(OneToMany.class, propertyArray);
		}
	}

}
