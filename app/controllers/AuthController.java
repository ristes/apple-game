package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Farmer;
import models.GameContext;
import play.cache.Cache;
import play.mvc.Controller;

public class AuthController extends Controller {

	public static void farmer() throws Exception {
		JsonController.toJson(getFarmer(), "gameDate", "field");
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
