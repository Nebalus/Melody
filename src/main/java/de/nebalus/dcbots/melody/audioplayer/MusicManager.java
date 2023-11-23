package de.nebalus.dcbots.melody.audioplayer;

import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.MelodyBotInstance;
import de.nebalus.framework.gfw.modules.dcbot.api.DCBotInstance;
import net.dv8tion.jda.api.entities.Guild;

public class MusicManager {

	private final MelodyBotInstance botInstance;
	private final ConcurrentHashMap<Long, GuildAudioController> controllers;

	public MusicManager(MelodyBotInstance botInstance) {
		this.botInstance = botInstance;
		controllers = new ConcurrentHashMap<>();
	}

	public GuildAudioController getController(long guildid) {
		if (controllers.containsKey(guildid)) {
			return controllers.get(guildid);
		}

		return createController(botInstance, guildid);
	}

	public GuildAudioController createController(MelodyBotInstance botInstance, long guildId) {
		Guild guild = botInstance.getJDA().getGuildById(guildId);
		GuildAudioController gac = new GuildAudioController(botInstance, guild);
		controllers.put(guildId, gac);
		return gac;
	}

	public boolean revokeController(long guildid) {
		if (!controllers.containsKey(guildid)) {
			return false;
		}

		controllers.remove(guildid);
		return true;
	}

	public long getGuildIdByPlayerHash(int hash) {
		for (GuildAudioController controller : controllers.values()) {
			if (controller.getPlayer().hashCode() == hash) {
				return controller.getGuildId();
			}
		}
		return -1;
	}
}
