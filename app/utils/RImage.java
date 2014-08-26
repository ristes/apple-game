package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import play.Play;
import play.cache.Cache;

public class RImage {

private final static String yml_file = "/data/rimages.yml";
	
	public static String get(String name) {
		return load_xml().get(name);
	}
	
	public static HashMap<String,String> load_xml() {
		HashMap<String, String> coefs = (HashMap<String, String>) Cache
				.get(yml_file);
		if (coefs == null) {
			File securityFile = Play.getFile(yml_file);
			InputStream input = null;
			try {
				input = new FileInputStream(securityFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Yaml yaml = new Yaml();
			coefs = (HashMap<String, String>) yaml.load(input);
		}
		return coefs;
	}
	
}
