package controllers;

import models.Farmer;
import exceptions.NotEnoughMoneyException;
import play.mvc.Catch;
import play.mvc.Controller;

public class GameController extends Controller {

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
		renderJSON(new ExceptionStatus(ex));
	}

	protected static Farmer checkFarmer() {
		Farmer farmer = AuthController.getFarmer();
		if (farmer == null) {
			error("Not logged in");
		}
		return farmer;
	}

}
