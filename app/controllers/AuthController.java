package controllers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import models.Farmer;
import play.cache.Cache;
import play.mvc.Controller;

public class AuthController extends Controller {

	public static void farmer() throws Exception {
		JsonController.toJson(getFarmer());
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

	protected static Long getFarmerId() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		return fid;
	}
}
