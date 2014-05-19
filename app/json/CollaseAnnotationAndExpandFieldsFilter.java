package json;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import play.db.jpa.Model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public class CollaseAnnotationAndExpandFieldsFilter extends
		SimpleBeanPropertyFilter {
	protected final Set<String> _propertiesToExpand;
	Class<? extends Annotation> annotationClass;

	public CollaseAnnotationAndExpandFieldsFilter(
			Class<? extends Annotation> ann, String... propertyArray) {
		this.annotationClass = ann;
		HashSet<String> properties = new HashSet<String>(propertyArray.length);
		Collections.addAll(properties, propertyArray);
		_propertiesToExpand = properties;
	}

	protected boolean include(BeanPropertyWriter writer) {
		return true;
	}

	protected boolean include(PropertyWriter writer) {
		return true;
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
						jgen.writeString(String.format("%s-%d", type, val.id));
					}
					jgen.writeEndArray();
					return true;
				}
			}
		}
		return false;
	}

	public void serializeAsField(Object pojo, JsonGenerator jgen,
			SerializerProvider provider, PropertyWriter writer)
			throws Exception {
		boolean handled = false;
		if (!_propertiesToExpand.contains(writer.getName())
				&& (writer instanceof BeanPropertyWriter)) {
			BeanPropertyWriter bwriter = (BeanPropertyWriter) writer;

			Object value = bwriter.get(pojo);
			if (bwriter.getAnnotation(OneToMany.class) != null
					|| bwriter.getAnnotation(ManyToMany.class) != null) {
				ParameterizedType type = (ParameterizedType) bwriter
						.getGenericPropertyType();
				handled = handleModelCollection(writer.getName(), type, value,
						jgen);
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
