package controllers;

import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import json.CollaseAnnotationAndExpandFieldsFilter;
import models.Farmer;
import play.mvc.Controller;
import play.mvc.Http;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dto.StatusDto;

public abstract class JsonController extends Controller {

	@JsonFilter("filter properties by name")
	class PropertyFilterMixIn {
	}
	
	protected static void statusToJson(StatusDto status, String... excludeFields) {
		
		toJson(status, excludeFields);
	}

	protected static void toJson(Object o, String... excludeFields) {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);
		GsonBuilder builder = new GsonBuilder().setDateFormat("dd.MM.yyyy");

		if (excludeFields != null && excludeFields.length > 0) {
			builder.setExclusionStrategies(ignoreManySet,
					new ExcludeFieldsStrategy(excludeFields));
		} else {
			builder.setExclusionStrategies(ignoreManySet);
		}
		Gson gson = builder.create();

		Appendable writer = new PrintStream(response.out);
		gson.toJson(o, writer);
		renderJSON("");

	}

	private static ExclusionStrategy ignoreManySet = new ExclusionStrategy() {

		@Override
		public boolean shouldSkipField(FieldAttributes attrs) {
			OneToMany otm = attrs.getAnnotation(OneToMany.class);
			ManyToMany mtm = attrs.getAnnotation(ManyToMany.class);
			if (otm != null || mtm != null) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			return false;
		}
	};

	private static class ExcludeFieldsStrategy implements ExclusionStrategy {

		Set<String> excludedFields = new HashSet<String>();

		public ExcludeFieldsStrategy(String[] excluded) {
			for (String s : excluded) {
				excludedFields.add(s);
			}
		}

		@Override
		public boolean shouldSkipField(FieldAttributes attrs) {
			if (excludedFields.contains(attrs.getName())) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public boolean shouldSkipClass(Class<?> clazz) {
			if (excludedFields.contains(clazz.getSimpleName() + ".class")) {
				return true;
			} else {
				return false;
			}
		}
	}

	protected static void farmerJson(Farmer farmer)
			throws JsonGenerationException, JsonMappingException, IOException {
		toJson(farmer, "field", "plantation");
	}

	protected static void statusJson(Farmer farmer)
			throws JsonGenerationException, JsonMappingException, IOException {
		StatusDto status = new StatusDto(farmer != null, null, null, farmer);
		toJson(status, "field", "plantation");
	}

	protected static void toJacksonJson(Object o, String... expandFields)
			throws JsonGenerationException, JsonMappingException, IOException {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);

		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
		mapper.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);

		FilterProvider filters = new SimpleFilterProvider().addFilter(
				"filter properties by name", new OneToManyFilter(expandFields));

		ObjectWriter jsonWriter = mapper.writer(filters);

		jsonWriter.writeValue(response.out, o);
		renderJSON("");

	}

	protected static String toJsonString(Object o, String... expandFields)
			throws JsonGenerationException, JsonMappingException, IOException {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);

		com.fasterxml.jackson.databind.ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
		mapper.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);

		FilterProvider filters = new SimpleFilterProvider().addFilter(
				"filter properties by name", new OneToManyFilter(expandFields));

		ObjectWriter jsonWriter = mapper.writer(filters);

		return jsonWriter.writeValueAsString(o);
	}

	static class OneToManyFilter extends CollaseAnnotationAndExpandFieldsFilter {

		public OneToManyFilter(String... propertyArray) {
			super(OneToMany.class, propertyArray);
		}
	}

}
