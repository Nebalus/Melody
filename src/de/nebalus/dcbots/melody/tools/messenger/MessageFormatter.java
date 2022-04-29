package de.nebalus.dcbots.melody.tools.messenger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import de.nebalus.dcbots.melody.core.constants.Build;
import org.json.JSONObject;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.datamanager.DataHelper;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

public class MessageFormatter {
	
	private HashMap<Language, JSONObject> messagecache; 
	private Melody melody = Melody.INSTANCE;
	
	public MessageFormatter() throws Exception {
		this.messagecache = new HashMap<Language, JSONObject>();
		
		for (Language language : Language.values()) {
			InputStream input = new FileInputStream(language.getFilePath());
			messagecache.put(language, new JSONObject(DataHelper.toString(input)));
        }
	}
	
	public String format(Guild guild, String key, Object... args) {
		GuildEntity guildentity = melody.entityMan.getGuildEntity(guild);
		Language language = guildentity.getLanguage();
		String message = "JSON-Error {"+key.toLowerCase()+"} - "+language.code;
	    try {
			JSONObject json = messagecache.get(language);
			message = json.getString(key.toLowerCase());
			if(language.equals(Language.GERMAN)) {
				message = message.replace("ss", "�");
				message = message.replace("ae", "�");
				message = message.replace("oe", "�");
				message = message.replace("ue", "�");
				message = message.replace("AE", "�");
				message = message.replace("OE", "�");
				message = message.replace("UE", "�");
				
				message = message.replace("[�]", "ss");
				message = message.replace("[�]", "ae");
				message = message.replace("[�]", "oe");
				message = message.replace("[�]", "ue");
			}
			
			message = message.replace("%botname%", Build.NAME);
			message = message.replace("%prefix%", guildentity.getPrefix());
			
			for(int i = 0; i < args.length; ++i) {
				message = message.replace("{" + i + "}", String.valueOf(args[i]));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return message;
	}
}