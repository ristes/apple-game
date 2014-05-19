package controllers;

import java.util.UUID;

import models.Day;
import models.Farmer;
import play.cache.Cache;
import play.mvc.Controller;

public class Crafty extends Controller {

	public static void iso() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			login();
		}
		render();
	}

	public static void shopwindow() {
		render();
	}

	public static void iso1() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			login();
		}
		render();
	}

	public static void login() {
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
		if (farmer == null) {
			farmer = new Farmer();
			Day start = Day.find("dayOrder", 0l).first();
			farmer.gameDate = start;
			farmer.balans = 1000000;
			farmer.eco_points = 100;
			farmer.username = username;
			farmer.password = password;
			farmer.save();
		}
		UUID id = UUID.randomUUID();
		Cache.add(id.toString(), farmer.id);
		session.put("farmer", id.toString());

		iso();
	}

	public static void logout() {
		String id = session.get("farmer");
		Long fid = (Long) Cache.get(id);
		Cache.delete(id);
		session.clear();
	}
}
