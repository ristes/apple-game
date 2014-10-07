package controllers;

import exceptions.UnauthorizedException;
import models.Farmer;
import play.mvc.Catch;
import play.mvc.Controller;
import play.mvc.Http;

public class GameController extends Controller {

	static final String[] FARMER_EXCLUDES = { "gameDate", "field",
			"weatherType", "plantation" };

	public static class ExceptionStatus {
		public boolean status = false;
		public String exception;
		public String message;

		public ExceptionStatus(Exception ex) {
			exception = ex.getClass().getSimpleName();
			message = ex.getMessage();
		}
	}

	@Catch
	public static void notEnoughMoney(final Exception ex) {
		response.status = Http.StatusCode.BAD_REQUEST;
		renderJSON(new ExceptionStatus(ex));
	}

	protected static Farmer checkFarmer() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			response.status = Http.StatusCode.UNAUTHORIZED;
			renderJSON(new ExceptionStatus(new UnauthorizedException()));
		}
		return farmer;
	}

}
