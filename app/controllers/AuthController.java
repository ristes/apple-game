package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.Query;

import models.Farmer;
import models.GameContext;
import models.ItemInstance;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Http;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AuthController extends Controller {

	public static void farmer() throws Exception {
		JsonController.toJson(getFarmer(), "field","gameDate","weatherType","plantation");
	}

	public static class PlantationDto {
		public String base;
		public double area;
		public String treePositions;
	}

	public static void plantation() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = getFarmer();
		if (farmer != null) {
			PlantationDto dto = new PlantationDto();
			dto.area = farmer.field.area;
			dto.base = farmer.field.plantation.base.name;
			dto.treePositions = farmer.field.plantation.treePositions;
			JsonController.toJson(dto);
		} else {
			response.status = 401;
			renderJSON(null);
		}
	}

	public static void items() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = getFarmer();
		if (farmer != null) {
			String sqlSelect = "select * from ItemInstance  where ownedBy_id=:farmer_id and id NOT IN (select DISTINCT(itemInstance_id) FROM ExecutedOperation where field_id=:field_id and  not(isnull(ItemInstance_id)))";
			Query query = JPA.em().createNativeQuery(sqlSelect,ItemInstance.class);
			query.setParameter("farmer_id", farmer.id);
			query.setParameter("field_id", farmer.field.id);
			List<ItemInstance> items = query.getResultList();
			renderJSON(items);
		} else {
			response.status = 401;
			renderJSON(null);
		}
	}

	protected static Farmer getFarmer() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		if (fid != null) {
			Farmer farmer = Farmer.findById(fid);
			return farmer;
		}
		return null;
	}

	public static void context() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
			redirect("/login");
		}
		JsonController.toJson(farmer, "field","gameDate","weatherType","plantation");
	}

	protected static GameContext getContext() {
		String id = session.get("farmer");
		Long cid = (Long) Cache.get(id);
		if (cid != null) {
			GameContext context = GameContext.findById(cid);
			return context;
		}
		return null;
	}

	protected static Long getFarmerId() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		return fid;
	}
}
