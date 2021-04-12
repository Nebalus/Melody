package de.pixelbeat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;


public class Json {

	public static boolean status = false;
	
	public static String path = "stats.json";
	
	public static File file = new File(path);
	
	public static void connect() {
		try {
			if(!file.exists()) {
				file.createNewFile();
				generateJsonFile(file);		
			
				JSONObject json = getJsonObject(file);
				json.put("PlayedMusicTime", 0l);
				json.put("TotalOnlineTime", 0l);
				FileWriter fw = new FileWriter(new File(file.getAbsolutePath()));
				fw.write(json.toString());
				fw.flush();
				fw.close();	
			}
			status = true;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static long getPlayedMusicTime() {
		JSONObject json;
		Long time = 0l;
		try {
			json = getJsonObject(file);
			time = json.getLong("PlayedMusicTime");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public static void setPlayedMusicTime(Long time) {
		try {
			JSONObject json = getJsonObject(file);
			json.put("PlayedMusicTime", time);
			FileWriter fw1 = new FileWriter(new File(file.getAbsolutePath()));
			fw1.write(json.toString());
			fw1.flush();
			fw1.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static long getTotalOnlineTime()  {
		JSONObject json;
		Long time = 0l;
		try {
			json = getJsonObject(file);
			time = json.getLong("TotalOnlineTime");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}
	
	public static void setTotalOnlineTime(Long time) {
		try {
			JSONObject json = getJsonObject(file);
			json.put("TotalOnlineTime", time);
			FileWriter fw1 = new FileWriter(new File(file.getAbsolutePath()));
			fw1.write(json.toString());
			fw1.flush();
			fw1.close();	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void generateJsonFile(File file) {
		try {
			FileWriter fw = new FileWriter(new File(file.getAbsolutePath()));
			fw.write("{}");
			fw.flush();
			fw.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static JSONObject getJsonObject(File jsonfile) throws Exception {
		String content = new String(Files.readAllBytes(Paths.get(jsonfile.toURI())), "UTF-8");
		JSONObject json = new JSONObject(content);
		return json;
	}
	
	/*
	 * 	try {
			Json.getJsonObject().put("PlayedMusicTime",0l);	
			FileWriter fw1 = new FileWriter(new File(Json.path));
			fw1.write(Json.getJsonObject()+ "");
			fw1.flush();
			fw1.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	 */
	public static boolean isConnected() {
		return status;
	}
	
	
}
