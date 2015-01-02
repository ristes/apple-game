package service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import models.Farmer;
import service.HumidityDropsService;
import service.HumidityGroovesService;
import service.HumidityService;
import dto.C;

public class HumidityServiceImpl implements HumidityService,
		HumidityGroovesService, HumidityDropsService {

	public void looses_q_less_water(Farmer farmer, Double variation) {

		farmer.productQuantity += (farmer.productQuantity * (variation / 100) * 1.2)
				/ (32.0 / 8);
	}

	public void looses_q_more_water(Farmer farmer, Double variation) {
		Double loose_q = 0.0;

		if (variation >= 10 && variation < 20) {
			loose_q = -8.0;
		} else if (variation >= 20 && variation < 30) {
			farmer.irrigation_misses++;
			loose_q = -10.0;
		} else if (variation >= 30 && variation < 40) {
			farmer.irrigation_misses++;
			loose_q = -16.0;
		} else if (variation >= 40 && variation < 50) {
			farmer.irrigation_misses += 2;
			loose_q = -23.0;
		} else if (variation >= 50) {
			farmer.irrigation_misses += 2;
			loose_q = -30.0;
		}
		farmer.productQuantity += farmer.productQuantity * (loose_q / 100.0)
				/ (32.0 / 8);
		// divide with the number of milestones every 8 days

	}

	public void looses_eco(Farmer farmer, Double variation) {
		Double loose_eco = 0.0;
		if (variation <= 0.0) {
			return;
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
		farmer.eco_points = (farmer.eco_points + (farmer.eco_points * (loose_eco / 100))
				/ (32.0 / 8));
	}

	/**
	 * determinate humidity level of soil 0 - dry 1 - normal 2 - low 3 - medium
	 * 4 - high 0, 1 and 2 have same visual component 3 and 4 different
	 * 
	 * @param farmer
	 * @return level coef
	 */

	public int humidityLevel(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Double lmtM1 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(0);
		Double lmt0 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(1);
		Double lmt1 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(2);
		Double lmt2 = coefs.get(C.KEY_HUMIDITY_LEVEL).get(3);
		Double cumVal = farmer.deltaCumulative;
		if (cumVal < lmtM1) {
			return 0;
		} else if (cumVal <= lmt0 && cumVal > lmtM1) {
			return 1;
		} else if (cumVal <= lmt1 && cumVal > lmt0) {
			return 2;
		} else if (cumVal >= lmt1 && cumVal < lmt2) {
			return 3;
		}
		return 4;
	}

	public Farmer humiditySetAppUrls(Farmer farmer) {
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

	public double varianceBrazdi(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		return farmer.deltaCumulative
				- coefs.get(C.KEY_GROOVES_HUM).get(c.get(Calendar.MONTH));
	}

	public double varianceDrops(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);
		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);
		return (farmer.deltaCumulative - coefs.get(C.KEY_DROPS_HUM).get(
				c.get(Calendar.MONTH)));
	}

	public int humidityLevelForSoil(int humidityLevel) {
		if (humidityLevel == 0 || humidityLevel == 1 || humidityLevel == 2) {
			return 1;
		} else if (humidityLevel == 3) {
			return 2;
		}
		return 3;
	}

	public void calculateGroovesVarianceImpact(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_hum = coefs.get(C.KEY_GROOVES_HUM).get(
				c.get(Calendar.MONTH));

		double impact = 0.0;
		if (coef_hum > 0.0) {
			if (coef_hum <= farmer.deltaCumulative) {
				impact = farmer.deltaCumulative * 100 / coef_hum - 100;
				looses_q_more_water(farmer, impact);
				looses_eco(farmer, impact);
			} else {
				impact = 100 - farmer.deltaCumulative * 100 / coef_hum;
				looses_q_less_water(farmer, impact);
			}
		}
	}

	public void calculateDropsVarianceImpact(Farmer farmer) {
		HashMap<String, ArrayList<Double>> coefs = YmlServiceImpl
				.load_hash(C.COEF_HUMIDITY_YML);

		Calendar c = Calendar.getInstance();
		c.setTime(farmer.gameDate.date);

		Double coef_hum = coefs.get(C.KEY_DROPS_HUM).get(c.get(Calendar.MONTH));
		double impact = 0.0;
		if (coef_hum > 0.0) {
			if (coef_hum <= farmer.deltaCumulative) {
				impact = farmer.deltaCumulative * 100 / coef_hum - 100;
				looses_q_more_water(farmer, impact);
				looses_eco(farmer, impact);
			} else {
				double deltaC = farmer.deltaCumulative;
				if (farmer.deltaCumulative < 0.0) {
					deltaC = 0.0;
				}
				impact = 100 - deltaC * 100 / coef_hum;
				looses_q_less_water(farmer, impact);
			}
		}
	}

}
