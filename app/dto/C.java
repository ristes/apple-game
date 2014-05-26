package dto;

public class C {
	
	public static String root_folder = "/public/images/";
	public static String irrigation_folder = "/public/images/game/operations/irrigation/";
	public static String landtreatman_folder = "/public/images/game/operations/landtreatman/";

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
		"/public/images/pocva-riste.png",
		irrigation_folder+"pocva-riste-low.png",
		irrigation_folder+"pocva-riste-medium.png",
		irrigation_folder+"pocva-riste-high.png",
		landtreatman_folder+"pocva-riste-green-medium.png",
		landtreatman_folder+"pocva-riste-green-high.png",
		landtreatman_folder+"pocva-riste-izorana.png",
		landtreatman_folder+"pocva-riste-izorana-humidity_medium.png",
		landtreatman_folder+"pocva-riste-izorana-humidity_high.png",
		root_folder + "pocva-riste-snow.png"
	};
	
}
