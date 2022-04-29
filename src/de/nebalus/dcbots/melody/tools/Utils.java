package de.nebalus.dcbots.melody.tools;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

public final class Utils {
	
	public static JSONObject getJsonObject(File jsonfile) throws Exception {
		String content = new String(Files.readAllBytes(Paths.get(jsonfile.toURI())), "UTF-8");
		JSONObject json = new JSONObject(content);
		return json;
	}
	
}
