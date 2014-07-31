package controllers;

import java.util.UUID;

import models.Day;
import models.Farmer;
import play.cache.Cache;
import play.i18n.Lang;
import play.mvc.Controller;
import service.ContextService;
import service.FarmerService;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;

public class Crafty extends Controller {

	public static void iso() {
		Farmer farmer = AuthController.getFarmer();

		if (farmer == null) {
			login("mk");
		}
		ContextService ctxService = new ContextServiceImpl();
		ctxService.evaluateState(farmer);
		render();
	}

	public static void shopwindow() {
		render();
	}

	public static void iso1() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			login("mk");
		}
		render("mk");
	}

	public static void login(String locale) {
		if (null == locale) {
			Lang.setDefaultLocale();
		} else {
			Lang.change(locale);
		}
		render();
	}

	public static void test() {
		render();
	}

	public static void authenticate(String username, String password) {
		Farmer farmer = null;
		try {
			farmer = Farmer.find("username=:username and password=:password")
					.setParameter("username", username)
					.setParameter("password", password).first();
		} catch (Exception ex) {
			System.out.println(ex);

		}
		System.out.println(farmer);
		FarmerService farmerService = new FarmerServiceImpl();
		if (farmer == null) {
			farmer = farmerService.buildInstance(username, password);
		}
		UUID id = UUID.randomUUID();
		Cache.add(id.toString(), farmer.id);
		Cache.add(farmer.username, id.toString());
		session.put("farmer", id.toString());

		iso();
	}

	public static void logout() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		Cache.delete(id);
		session.clear();
	}

	public static void teststate() {
		render();
	}
}
