package service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.Yaml;

import play.Play;
import play.cache.Cache;
import dto.C;
import service.YmlService;

public class YmlServiceImpl implements YmlService{

	public static HashMap<String, ArrayList<Double>> load_hash(String keyName) {
		HashMap<String, ArrayList<Double>> coefs = (HashMap<String, ArrayList<Double>>) Cache
				.get(keyName);
		if (coefs == null) {
			File securityFile = Play.getFile(keyName);
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
	
	public static HashMap<Integer, ArrayList<Double>> load_hash_key_int(String keyName) {
		HashMap<Integer, ArrayList<Double>> coefs = (HashMap<Integer, ArrayList<Double>>) Cache
				.get(keyName);
		if (coefs == null) {
			File securityFile = Play.getFile(keyName);
			InputStream input = null;
			try {
				input = new FileInputStream(securityFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Yaml yaml = new Yaml();
			coefs = (HashMap<Integer, ArrayList<Double>>) yaml.load(input);
		}
		return coefs;
	}
	
}
