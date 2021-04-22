package de.pixelbeat.entities;

import java.util.concurrent.ConcurrentHashMap;

import de.pixelbeat.PixelBeat;

public class EntityManager {

	public ConcurrentHashMap<Long, EntityController> controller;
	
	public EntityManager() {
		this.controller = new ConcurrentHashMap<Long, EntityController>();
	}
	
	public EntityController getController(long guildid) {
		EntityController ec = null;
		
		if(this.controller.containsKey(guildid)) {
			ec = this.controller.get(guildid);
		}else {
			ec = new EntityController(PixelBeat.INSTANCE.shardMan.getGuildById(guildid));
		
			this.controller.put(guildid, ec);
		}
		return ec;
	}
}
