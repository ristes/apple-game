package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import models.Farmer;
import dto.C;
import play.Play;
import play.cache.Cache;
import play.mvc.Controller;

public class HumidityController extends Controller {

	/**
	 * load yml file with coefs
	 * 
	 * @return
	 */

	public static HashMap<String, ArrayList<Double>> load_hash() {
		String key = "COEFFICIENTS";
		HashMap<String, ArrayList<Double>> coefs = (HashMap<String, ArrayList<Double>>) Cache
				.get(key);
		if (coefs == null) {
			File securityFile = Play.getFile(C.COEF_HUMIDITY_YML);
			InputStream input = null;
			try {
				input = new FileInputStream(securityFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Yaml yaml = new Yaml();
			coefs = (HashMap<String, ArrayList<Double>>) yaml.load(input);
		}
		return coefs;
	}

	public static int drops_irrigation_delta_impact_quantity(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_eva = coefs.get(C.KEY_DROPS_EVAP)
				.get(c.get(Calendar.MONTH));
		Double coef_hum = coefs.get(C.KEY_DROPS_HUM).get(c.get(Calendar.MONTH));

		if (coef_hum > 0.0) {
			double impact = farmer.deltaCumulative * 100 / coef_hum - 100;
			return looses_q(farmer.productQuantity, impact);
		}
		return farmer.productQuantity;
	}

	public static double drops_irrigation_delta_impact_eco_point(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_eva = coefs.get(C.KEY_DROPS_EVAP)
				.get(c.get(Calendar.MONTH));
		Double coef_hum = coefs.get(C.KEY_DROPS_HUM).get(c.get(Calendar.MONTH));
		double impact = 0.0;
		if (coef_hum > 0.0) {
			impact = farmer.deltaCumulative * 100 / coef_hum - 100;
		}
		return looses_eco(farmer.eco_points, impact);
	}

	public static int brazdi_irrigation_delta_impact_quantity(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_eva = coefs.get(C.KEY_GROOVES_EVAP).get(
				c.get(Calendar.MONTH));
		Double coef_hum = coefs.get(C.KEY_GROOVES_HUM).get(
				c.get(Calendar.MONTH));
		if (coef_hum > 0.0) {
			double impact = farmer.deltaCumulative * 100 / coef_hum - 100;
			return looses_q(farmer.productQuantity, impact);
		}
		return farmer.productQuantity;
	}

	public static double brazdi_irrigation_delta_impact_eco_point(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_eva = coefs.get(C.KEY_GROOVES_EVAP).get(
				c.get(Calendar.MONTH));
		Double coef_hum = coefs.get(C.KEY_GROOVES_HUM).get(
				c.get(Calendar.MONTH));
		if (coef_hum > 0.0) {
			double impact = farmer.deltaCumulative * 100 / coef_hum - 100;
			return looses_eco(farmer.eco_points, impact);
		}
		return farmer.eco_points;
	}

	public static int looses_q(int value, Double variation) {
		Double loose_q = 0.0;
		if (variation > 0.0) {
			if (variation >= 10 && variation < 20) {
				loose_q = -12.0;
			} else if (variation >= 20 && variation < 30) {
				loose_q = -20.0;
			} else if (variation >= 30 && variation < 40) {
				loose_q = -26.0;
			} else if (variation >= 40 && variation < 50) {
				loose_q = -33.0;
			} else if (variation >= 50) {
				loose_q = -50.0;
			}
			// divide with the number of milestones every 8 days
			return (int) (value + ((value * (loose_q/100.0)) / (365.0 / 8)));
		}

		return (int) (value + (value * (variation / 100) * 1.2) / (365.0 / 8));

	}

	public static double looses_eco(double value, Double variation) {
		Double loose_eco = 0.0;
		if (variation <= 0.0) {
			return value;
		}
		if (variation >= 10 && variation < 20) {
			loose_eco = -5.0;
		} else if (variation >= 20 && variation < 30) {
			loose_eco = -7.0;
		} else if (variation >= 30 && variation < 40) {
			loose_eco = -10.0;
		} else if (variation >= 40 && variation < 50) {
			loose_eco = -13.0;
		} else if (variation >= 50) {
			loose_eco = -15.0;
		}
		return (double) (value + (value * (loose_eco/100)) / (365.0 / 8));
	}

	/**
	 * determinate humidity level of soil  
	 * 0 - dry 
	 * 1 - normal 
	 * 2 - low 
	 * 3 - medium 
	 * 4 - high
	 * 0, 1 and 2 have same visual component
	 * 3 and 4 different
	 * @param farmer
	 * @return level coef
	 */

	public static int humidityLevel(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();
		Double lmtM1 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(0);
		Double lmt0 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(1);
		Double lmt1 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(2);
		Double lmt2 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(3);
		Double cumVal = farmer.deltaCumulative;
		if (cumVal < lmtM1) {
			return 0;
		}
		else if (cumVal < lmt0 && cumVal > lmtM1) {
			return 1;
		}
		else if (cumVal < lmt1 && cumVal > lmt0) {
			return 2;
		}
		else if (cumVal >= lmt1 && cumVal < lmt2) {
			return 3;
		}
		return 4;
	}

	public static Farmer humiditySetAppUrls(Farmer farmer) {
		int level = humidityLevel(farmer);
		if (level == 1) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_low];
		}
		if (level == 2) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_normal];
		}
		if (level == 3) {
			farmer.soil_url = C.soil_urls[C.soil_irrigated_brazdi_high];
		}
		return farmer;
	}
	
	public static double varianceBrazdi(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		return  (farmer.deltaCumulative - coefs.get(C.KEY_GROOVES_HUM).get(c.get(Calendar.MONTH)));
	}
	
	public static double varianceDrops(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = load_hash();
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		return  (farmer.deltaCumulative - coefs.get(C.KEY_DROPS_HUM).get(c.get(Calendar.MONTH)));
	}

	public static int humidityLevelForSoil(int humidityLevel) {
		if (humidityLevel==0 || humidityLevel==1 || humidityLevel==2) {
			return 1;
		}
		else if (humidityLevel==3) {
			return 2;
		} 
		return 3;
	}
}
