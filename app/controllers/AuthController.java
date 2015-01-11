package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.ivy.core.module.status.Status;

import models.Farmer;
import models.GameContext;
import models.ItemInstance;
import models.PlantType;
import models.PlantationSeedling;
import play.cache.Cache;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Http;
import service.ServiceInjector;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import dao.DaoInjector;
import dao.ItemsDao;
import dao.impl.ItemsDaoImpl;
import dto.PlantationDto;
import dto.StatusDto;

public class AuthController extends GameController {

	public static void farmer() throws Exception {
		// JsonController.farmerJson(getFarmer());
		JsonController.statusJson(getFarmer());
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
			dto.plantTypes = new ArrayList<PlantType>();
			for (PlantationSeedling ps : farmer.field.plantation.seedlings) {
				dto.plantTypes.add(ps.seedling.type);
			}
			JsonController.toJson(dto, "period");
		} else {
			renderJSON("");
		}
	}

	public static void items() throws JsonGenerationException,
			JsonMappingException, IOException {
		Farmer farmer = getFarmer();
		if (farmer != null) {
			renderJSON(DaoInjector.itemsDao.getFarmerCurrentItems(farmer));
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
			if (farmer.is_active) {
				ServiceInjector.contextService.onLoadEvaluateState(farmer);
				return farmer;
			}
		}
		return null;
	}

	public static void context() throws IOException {
		Farmer farmer = getFarmer();
		if (farmer == null) {
			redirect("/login");
		}
		JsonController.statusJson(farmer);
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
