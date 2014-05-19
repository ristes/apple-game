package controllers;

import java.lang.reflect.Method;
import java.util.List;

import play.db.jpa.Model;
import play.mvc.Controller;

public class ModelStore extends Controller {

	public static void all(String clazz) throws Exception {
		Class<Model> clazzInstance = (Class<Model>) Class.forName(clazz);

		Method findAll = clazzInstance.getMethod("findAll", new Class[] {});
		List<?> all = (List<?>) findAll.invoke(null, new Object[] {});

		JsonController.toJson(all);
	}

	public static void instance(String clazz, Long id) throws Exception {
		Class<Model> clazzInstance = (Class<Model>) Class.forName(clazz);

		Method findAll = clazzInstance.getMethod("findById",
				new Class[] { Object.class });
		Model all = (Model) findAll.invoke(null, new Object[] { id });

		JsonController.toJson(all);
	}

}
