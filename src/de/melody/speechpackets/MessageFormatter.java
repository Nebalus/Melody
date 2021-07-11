package de.melody.speechpackets;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.ResultSet;
import java.util.HashMap;

import org.json.JSONObject;

import de.melody.Json;
import de.melody.Melody;
import de.melody.entities.GuildEntity;

public class MessageFormatter {
	
	private HashMap<Languages, JSONObject> getJSONMessage = new HashMap<Languages, JSONObject>();
	private Melody melody = Melody.INSTANCE;
	public MessageFormatter() {
		for (Languages language : Languages.values()) {
			try {
				InputStream link = getClass().getResourceAsStream("/de/melody/speechpackets/"+language.getFileName());
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
	    	Languages language = melody.entityManager.getGuildEntity(guildid).getLanguage();
			JSONObject json = getJSONMessage.get(language);
			message = json.getString(key);
			for(int i = 0; i < args.length; ++i) {
				message = message.replace("{" + i + "}", String.valueOf(args[i]));
		    }
			if(language.equals(Languages.GERMAN)) {
				message = message.replace("ae", "ä");
				message = message.replace("oe", "ö");
				message = message.replace("ue", "ü");
				message = message.replace("AE", "Ä");
				message = message.replace("OE", "Ö");
				message = message.replace("UE", "Ü");
				
				message = message.replace("[ä]", "ae");
				message = message.replace("[ö]", "oe");
				message = message.replace("[ü]", "ue");
				System.out.println("test");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return message;
	}
}