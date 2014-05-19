package controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import models.TerrainAnalysis;
import models.TerrainFeature;
import models.TerrainFeatureCategory;
import play.Play;
import play.mvc.Controller;

public class Import extends Controller {

	public static void terrainFeatures() throws IOException {
		File file = Play.getFile("/data/terrainFeatures");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split("\\t");
				Long analysisId = Long.parseLong(parts[0]);
				Long featureCategoryId = Long.parseLong(parts[1]);
				TerrainFeature tf = new TerrainFeature();
				tf.analysis = TerrainAnalysis.findById(analysisId);
				tf.category = TerrainFeatureCategory
						.findById(featureCategoryId);
				tf.value = parts[2];
				tf.save();
			}
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}

}
