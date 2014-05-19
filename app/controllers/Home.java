package controllers;

import models.Farmer;
import models.Store;
import play.mvc.Controller;

/**
 * Imam farmer
 * 
 * @author
 * 
 */
public class Home extends Controller {

	private static Farmer currentFarmer;

	public static void ownedItems() {

	}

	public static void ownedFields() {

	}

	public static void allStores() throws Exception {
		JsonController.toJson(Store.all().fetch());
	}

	/**
	 * Vrakja json so 7 weather instanci, za 3-te prethodni dena i tekovniot se
	 * sigurni vrednosti, a za narednite 3 e prognoza
	 */
	public static void weather() {

	}

}
