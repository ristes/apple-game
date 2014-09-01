package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.types.Photo;
import com.restfb.types.User;
import com.restfb.*;

import dto.DiseaseOccurenceProb;
import dto.DiseaseStatusDto;
import exceptions.NotEnoughMoneyException;
import models.Day;
import models.Farmer;
import play.cache.Cache;
import play.libs.WS;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Finally;
import play.mvc.With;
import service.ContextService;
import service.FarmerService;
import service.MoneyTransactionService;
import service.impl.ContextServiceImpl;
import service.impl.FarmerServiceImpl;
import service.impl.TransactionServiceImpl;
import utils.Constants;
import utils.FacebookSignedRequest;

public class FbController extends Controller {
	public static void login(String signed_request) {

		FacebookClient facebookClient;
		FacebookSignedRequest facebookSR = null;

		try {
			facebookSR = FacebookSignedRequest
					.getFacebookSignedRequest(signed_request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String oauthToken = facebookSR.getOauth_token();
		if (oauthToken == null) {

			String authURL = Constants.API_KEY;

			render(authURL);
		} else {

			facebookClient = new DefaultFacebookClient(oauthToken);

			User user = facebookClient.fetchObject("me", User.class);

			JsonElement profilepic = WS
					.url("https://graph.facebook.com/" + user.getId()
							+ "/picture?type=normal&redirect=false").get()
					.getJson();

			String picture = profilepic.getAsJsonObject().get("data")
					.getAsJsonObject().get("url").getAsString();
			FarmerService farmerService = new FarmerServiceImpl();
			Farmer farmer = farmerService.buildFbInstance(user.getId(),
					oauthToken, user.getFirstName(), user.getLastName(),
					user.getEmail(), picture);

			UUID id = UUID.randomUUID();
			Cache.add(id.toString(), farmer.id);
			Cache.add(farmer.username, id.toString());
			session.put("farmer", id.toString());

			redirect("/");
		}
	}

	public static void checkShared() {
		Farmer farmer = AuthController.getFarmer();
		boolean status = true;
		if (farmer != null) {
			status = farmer.sharedApp;
		}

		renderJSON("{\"status\":" + status + "}");
	}

	// TODO metod koj se povikuva otkako korisnikot ke napravi share na
	// facebook. Valda treba da dobie eko poeni ili pari...
	public static void shareGame() {
		Farmer farmer = AuthController.getFarmer();
		farmer.sharedApp = true;
		MoneyTransactionService moneyS = new TransactionServiceImpl();
		try {
			moneyS.commitMoneyTransaction(farmer, Constants.money_obtained_by_share);
		} catch (NotEnoughMoneyException ex) {
			ex.printStackTrace();
		}
		farmer.save();
	}
}
