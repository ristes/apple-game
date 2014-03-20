package controllers;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;
import play.mvc.Controller;
import play.mvc.Http;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public abstract class JsonController extends Controller {

	@JsonFilter("filter properties by name")
	class PropertyFilterMixIn {
	}

	protected static void toJson(Object o, String... excludeFields)
			throws JsonGenerationException, JsonMappingException, IOException {
		String encoding = Http.Response.current().encoding;
		response.setContentTypeIfNotSet("application/json; charset=" + encoding);

		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("dd.MM.yyyy"));
		mapper.addMixInAnnotations(Object.class, PropertyFilterMixIn.class);

		FilterProvider filters = new SimpleFilterProvider()
				.addFilter("filter properties by name", new OneToManyFilter(
						excludeFields));

		ObjectWriter jsonWriter = mapper.writer(filters);

		jsonWriter.writeValue(response.out, o);
		renderJSON("");

	}

	static class AnnotationAndIngoreFieldsFilter extends
			SimpleBeanPropertyFilter {
		protected final Set<String> _propertiesToExclude;
		Class<? extends Annotation> annotationClass;

		public AnnotationAndIngoreFieldsFilter(Class<? extends Annotation> ann,
				String... propertyArray) {
			this.annotationClass = ann;
			HashSet<String> properties = new HashSet<String>(
					propertyArray.length);
			Collections.addAll(properties, propertyArray);
			_propertiesToExclude = properties;
		}

		@Override
		protected boolean include(BeanPropertyWriter writer) {
			return !_propertiesToExclude.contains(writer.getName())
					&& writer.getAnnotation(annotationClass) == null;
		}

		@Override
		protected boolean include(PropertyWriter writer) {
			if (writer instanceof BeanPropertyWriter) {
				return include((BeanPropertyWriter) writer);
			}
			return !_propertiesToExclude.contains(writer.getName());
		}

		private boolean handleModelCollection(String name,
				ParameterizedType fieldType, Object value, JsonGenerator jgen)
				throws Exception {
			if (value != null) {
				Collection values = (Collection) value;
				Type[] generics = fieldType.getActualTypeArguments();
				if (generics != null && generics.length == 1) {
					if (Model.class.isAssignableFrom((Class) generics[0])) {
						String type = ((Class) generics[0]).getSimpleName();
						Collection<Model> entities = values;
						jgen.writeFieldName(name);
						jgen.writeStartArray();
						for (Model val : entities) {
							jgen.writeString(String.format("%s-%d", type,
									val.id));
						}
						jgen.writeEndArray();
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public void serializeAsField(Object pojo, JsonGenerator jgen,
				SerializerProvider provider, PropertyWriter writer)
				throws Exception {
			boolean handled = false;
			if (writer instanceof BeanPropertyWriter) {
				BeanPropertyWriter bwriter = (BeanPropertyWriter) writer;

				Object value = bwriter.get(pojo);
				if (bwriter.getAnnotation(OneToMany.class) != null
						|| bwriter.getAnnotation(ManyToMany.class) != null) {
					ParameterizedType type = (ParameterizedType) bwriter
							.getGenericPropertyType();
					handled = handleModelCollection(writer.getName(), type,
							value, jgen);
				} else if (bwriter.getAnnotation(OneToOne.class) != null
						|| bwriter.getAnnotation(ManyToOne.class) != null) {
					Model val = (Model) value;
					if (val != null) {
						jgen.writeFieldName(bwriter.getName());
						jgen.writeString(String.format("%s-%d", val.getClass()
								.getSimpleName(), val.id));
						handled = true;
					}
				} else if ("id".equals(bwriter.getName())) {
					jgen.writeFieldName(bwriter.getName());
					jgen.writeString(String.format("%s-%d", pojo.getClass()
							.getSimpleName(), value));
					handled = true;
				}
			}

			if (!handled) {
				if (include(writer)) {
					writer.serializeAsField(pojo, jgen, provider);
				} else if (!jgen.canOmitFields()) { // since 2.3
					writer.serializeAsOmittedField(pojo, jgen, provider);
				}
			}
		}
	}

	static class OneToManyFilter extends AnnotationAndIngoreFieldsFilter {

		public OneToManyFilter(String... propertyArray) {
			super(OneToMany.class, propertyArray);
		}
	}

}
