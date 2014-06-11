package dto;

public class C {
	
	public static final String COEF_HUMIDITY_YML = "/data/coefs_irrigation.yml";
	
	public static final String COEF_SALES_YML = "/data/coefs_sale.yml";
	
	public static final String KEY_RAIN_COEFS = "coef_rain_avgs";
	
	public static final String KEY_GROOVES_EVAP = "coef_grooves_evaporation";
	public static final String KEY_GROOVES_HUM = "coef_grooves_humidity";
	
	public static final String KEY_DROPS_EVAP = "coef_drops_evaporation";
	public static final String KEY_DROPS_HUM = "coef_drops_humidity";
	
	public static final String KEY_DIGGING_COEF = "coef_digging_coef";
	
	public static final String KEY_ONE_HOUR_IRRIGATION_VALUES = "coef_mm_one_hour_irrigation";
	public static final String KEY_PRICE_IRRIGATION_VALUES = "coef_price_one_ar_irrigation";
	
	public static final String KEY_SALE_PRICES = "apple_sale_per_month";
	
	public static final String KEY_HUMIDITY_LEVEL = "coef_humidity_level";
	
	public static final String KEY_MIN_HUMIDITY = "min_humidity";
	
	public static final String KEY_SALES_APPLES_PER_MONTH = "";
	
	public static final int ENUM_GROOVES = 0;
	public static final int ENUM_DROPS = 1;
	
	public static final long WEATHER_TYPE_SUNNY = 1;
	public static final long WEATHER_TYPE_CLOUDY = 2;
	public static final long WEATHER_TYPE_RAINY = 3;
	public static final long WEATHER_TYPE_ICY = 4;

	public static String root_folder = "/public/images/";
	public static String irrigation_folder = "/public/images/game/soil/irrigation/";
	public static String landtreatman_folder = "/public/images/game/soil/landtreatman/";

	public static int soil_normal = 0;
	public static int soil_irrigated_brazdi_low = 1;
	public static int soil_irrigated_brazdi_normal = 2;
	public static int soil_irrigated_brazdi_high = 3;
	public static int soil_irrigated_brazdi_high_grass_medium = 4;
	public static int soil_irrigated_brazdi_high_grass_high = 5;
	public static int soil_irrigated_normal_plowing_normal = 6;
	public static int soil_irrigated_medium_plowing_normal = 7;
	public static int soil_irrigated_high_plowing_normal = 8;
	public static int soil_with_snow = 9;
	public static String[] soil_urls = {
		"/public/images/soil/soil.png",
		irrigation_folder+"soil-low.png",
		irrigation_folder+"soil-medium.png",
		irrigation_folder+"soil-high.png",
		landtreatman_folder+"soil-green-medium.png",
		landtreatman_folder+"soil-green-high.png",
		landtreatman_folder+"soil-izorana.png",
		landtreatman_folder+"soil-izorana-humidity_medium.png",
		landtreatman_folder+"soil-izorana-humidity_high.png",
		root_folder + "soil-snow.png"
	};
	
//	public static Double[] coef_drops_evaporation = {0.0,0.0,0.0,0.8,0.7,0.6,0.5,0.5,0.6,0.0,0.0,0.0};
//	
//	public static Double[] coef_drops_humidity = {0.0,0.0,0.0,3.76,11.12,19.2,21.0,19.6,14.4,0.0,0.0,0.0};
//	
//	public static Double[] coef_grooves_evaporation = {0.0,0.0,0.0,0.8,0.7,0.6,0.5,0.5,0.6,0.0,0.0,0.0};
//		
//	public static Double[] coef_grooves_humidity = {0.0, 0.0,0.0,6.5,19.8,34.0,37.0,35.0,25.6,0.0,0.0,0.0};
	
	
	
	
}
