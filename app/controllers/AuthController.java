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

import dao.ItemsDao;
import dao.impl.ItemsDaoImpl;
import dto.PlantationDto;

public class AuthController extends Controller {

	public static void farmer() throws Exception {
		JsonController.farmerJson(getFarmer());
	}

	

	public static void plantation() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = getFarmer();
		if (farmer != null && farmer.field != null
				&& farmer.field.plantation != null
				&& farmer.field.plantation.base != null) {
			PlantationDto dto = new PlantationDto();

			dto.area = farmer.field.area;
			dto.base = farmer.field.plantation.base.name;
			dto.treePositions = farmer.field.plantation.treePositions;
			JsonController.toJson(dto);
		} else {
			renderJSON("");
		}
	}

	public static void items() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = getFarmer();
		if (farmer != null) {
			ItemsDao itemsDao = new ItemsDaoImpl();
			renderJSON(itemsDao.getBoughtAndUnusedItems(farmer));
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
		JsonController.farmerJson(farmer);
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
