package dto;

public class C {
	
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
	
}
