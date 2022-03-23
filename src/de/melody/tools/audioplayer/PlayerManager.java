package de.melody.tools.audioplayer;

import java.util.concurrent.ConcurrentHashMap;

import de.melody.core.Melody;

public final class PlayerManager {

	private ConcurrentHashMap<Long, AudioController> controller;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, AudioController>();
	}
	
	public AudioController getController(long guildid) {
		AudioController ac = null;		
		if(this.controller.containsKey(guildid)) {
			ac = this.controller.get(guildid);
		}else {
			ac = new AudioController(Melody.getGuildById(guildid));
			this.controller.put(guildid, ac);
		}
		return ac;
	}
}
