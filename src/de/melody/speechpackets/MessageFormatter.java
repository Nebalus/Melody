package de.melody.speechpackets;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.json.JSONObject;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;

public class MessageFormatter {
	
	private HashMap<Languages, JSONObject> getJSONMessage = new HashMap<Languages, JSONObject>();
	private Melody melody = Melody.INSTANCE;
	public MessageFormatter() {
		for (Languages language : Languages.values()) {
			try {
				InputStream link = getClass().getResourceAsStream("/de/melody/speechpackets/"+language.getFileName());
				File file = new File(Constants.TEMP_DIRECTORY + "/" +language.getFileName());
				Files.copy(link, file.getAbsoluteFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
				getJSONMessage.put(language, Utils.getJsonObject(file));
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
	}
	
	public String format(Guild guild, String key, Object... args) {
		GuildEntity guildentity = melody.getEntityManager().getGuildEntity(guild);
		Languages language = guildentity.getLanguage();
		String message = "JSON-Error {"+key+"} - "+language.code;
	    try {
			JSONObject json = getJSONMessage.get(language);
			message = json.getString(key);
			if(language.equals(Languages.GERMAN)) {
				message = message.replace("ss", "ß");
				message = message.replace("ae", "ä");
				message = message.replace("oe", "ö");
				message = message.replace("ue", "ü");
				message = message.replace("AE", "Ä");
				message = message.replace("OE", "Ö");
				message = message.replace("UE", "Ü");
				
				message = message.replace("[ß]", "ss");
				message = message.replace("[ä]", "ae");
				message = message.replace("[ö]", "oe");
				message = message.replace("[ü]", "ue");
			}
			message = message.replace("[botname]", Constants.BUILDNAME);
			message = message.replace("[prefix]", guildentity.getPrefix());
			
			for(int i = 0; i < args.length; ++i) {
				message = message.replace("{" + i + "}", String.valueOf(args[i]));
				//Trying to make a pronoun system example 1 track, 2 tracks
				/*
				Long count;
				if((count = Long.getLong((String) args[i])) != null) {
					if(count == 1) {
						//sp stands for single pronoun
						message = message.replace("{" + i + "-sp}", String.valueOf(args[i]));	
						message = message.replace("{" + i + "-mp}", "");	
					}else {
						//mp stands for multiple pronoun
						message = message.replace("{" + i + "-mp}", String.valueOf(args[i]));	
						message = message.replace("{" + i + "-sp}", "");	
					}
				}
				*/
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return message;
	}
}