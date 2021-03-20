package de.pixelbeat.cache;

import net.dv8tion.jda.api.entities.Guild;

public class CacheController {

	private Cache cache;
	private Guild guild;
	
	public CacheController(Guild guild) {
		this.guild = guild;
		this.cache = new Cache(this);
	}
	
	public Cache getCache() {
		return cache;
	}
	
	public Guild getGuild() {
		return guild;
	}
}
