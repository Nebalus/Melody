package de.pixelbeat.speechpackets;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.HashMap;

import org.json.JSONObject;

import de.pixelbeat.Json;
import de.pixelbeat.LiteSQL;

public class MessageFormatter {
	
	private HashMap<Languages, JSONObject> getJSONMessage = new HashMap<Languages, JSONObject>();
	
	public MessageFormatter() {
		for (Languages language : Languages.values()) {
			try {
				InputStream link = getClass().getResourceAsStream("/de/pixelbeat/speechpackets/"+language.getFileName());
				File file = new File(language.getFileName());
				Files.copy(link, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				
				getJSONMessage.put(language, Json.getJsonObject(file));
					
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public String format(Long guildid, String key, Object... args) {
		String message = "JSON-Error {"+key+"}";
	    try {
			JSONObject json = getJSONMessage.get(getLanguageFromGuild(guildid));
			message = json.getString(key);
			for(int i = 0; i < args.length; ++i) {
				message = message.replace("{" + i + "}", String.valueOf(args[i]));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return message;
	}
	private Languages getLanguageFromGuild(Long guildid) {
		try {
			ResultSet set = LiteSQL.onQuery("SELECT language FROM general WHERE guildid = "+guildid);
			if(set.next()) {
				return Languages.getLanguage(set.getString("language"));
			}
		} catch (Exception e) {}
		return Languages.ENGLISH;
	}
}