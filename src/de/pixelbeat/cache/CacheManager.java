package de.pixelbeat.cache;

import java.util.concurrent.ConcurrentHashMap;

import de.pixelbeat.PixelBeat;

public class CacheManager {

	public ConcurrentHashMap<Long, CacheController> controller;
	
	public CacheManager() {
		this.controller = new ConcurrentHashMap<Long, CacheController>();
	}
	
	public CacheController getController(long guildid) {
		CacheController mc = null;
		
		if(this.controller.containsKey(guildid)) {
			mc = this.controller.get(guildid);
		}
		else {
			mc = new CacheController(PixelBeat.INSTANCE.shardMan.getGuildById(guildid));
		
			this.controller.put(guildid, mc);
		}
		return mc;
	}
	
	

}
