package controllers;

import java.util.UUID;

import models.Farmer;
import play.cache.Cache;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.Http;
import service.ContextService;
import service.FarmerService;
import service.ServiceInjector;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;

public class Crafty extends Controller {

	public static void iso() {
		Farmer farmer = AuthController.getFarmer();

		if (farmer == null) {
			login("en",null);
		}
		ServiceInjector.contextService.setAndCheckLastLoginDate(farmer);
		render();
	}

	public static void shopwindow() {
		render();
	}

	public static void iso1() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			login("en",null);
		}
		render("en");
	}

	public static void login(String locale, String message) {
		if (null == locale) {
			Lang.setDefaultLocale();
		} else {
			Lang.change(locale);
		}
		render(message);
	}

	public static void register(String message) {
		render(message);
	}

	public static void test() {
		render();
	}

	public static void saveregistration(String username, String password,
			String passwordRepeat, String name, String surname, String email) {
		Farmer farmer = null;
		try {
			farmer = Farmer.find("username=:username and password=:password")
					.setParameter("username", username)
					.setParameter("password", password).first();
		} catch (Exception ex) {
			System.out.println(ex);

		}
		if (farmer != null) {
			register("The user already exists! Try another username.");
		} else {
			if (!password.equals(passwordRepeat)) {
				register("Passwords do not match!");
			} else {
				farmer = ServiceInjector.farmerService.buildInstance(username,
						password);
				farmer.name = name;
				farmer.surname = surname;
				farmer.email = email;
				farmer.save();
			}
		}
		login("en","");
	}

	public static void authenticate(String username, String password) {
		Farmer farmer = null;
		try {
			farmer = Farmer.find("username=:username and password=:password and is_active=:is_active")
					.setParameter("username", username)
					.setParameter("password", password)
					.setParameter("is_active",true).first();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());

		}
		FarmerService farmerService = new FarmerServiceImpl();
		if (farmer == null) {
			login("en","Username or password are invalid!");
		}
		UUID id = UUID.randomUUID();
		Cache.add(id.toString(), farmer.id);
		Cache.add(farmer.username, id.toString());
		session.put("farmer", id.toString());

		iso();
	}

	public static void logout() {
		String id = session.get("farmer");
		Cache.delete(id);
		session.clear();
	}

	public static void teststate() {
		render();
	}
}
