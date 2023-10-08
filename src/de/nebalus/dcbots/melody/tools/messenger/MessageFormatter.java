package de.nebalus.dcbots.melody.tools.messenger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.json.JSONObject;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.datamanager.DataHelper;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

public class MessageFormatter {

	private HashMap<Language, JSONObject> messagecache;

	public MessageFormatter() throws Exception {
		messagecache = new HashMap<>();

		for (Language language : Language.values()) {
			InputStream input = new FileInputStream(language.getFilePath());
			messagecache.put(language, new JSONObject(DataHelper.toString(input)));
		}
	}

	public String format(Guild guild, String key, Object... args) {
		GuildEntity guildentity = Melody.getEntityManager().getGuildEntity(guild);
		Language lang = guildentity.getLanguage();

		return format(lang, key, args);
	}

	public String format(String key, Object... args) {
		return format(Settings.DEFAULT_LANGUAGE, key, args);
	}

	@SuppressWarnings("incomplete-switch")
	public String format(Language lang, String key, Object... args) {
		String message = "JSON-Error {" + key.toLowerCase() + "} - " + lang.code;
		try {
			JSONObject json = messagecache.get(lang);
			message = json.getString(key.toLowerCase());

			switch (lang) {
			case GERMAN:
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
				break;
			}

			message = message.replace("%botname%", Build.NAME);

			for (int i = 0; i < args.length; ++i) {
				message = message.replace("{" + i + "}", String.valueOf(args[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}
}