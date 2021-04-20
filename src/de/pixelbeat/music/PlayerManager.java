package de.pixelbeat.music;

import java.util.concurrent.ConcurrentHashMap;

import de.pixelbeat.PixelBeat;


public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, MusicController>();
	}
	
	public MusicController getController(long guildid) {
		MusicController mc = null;
		
		if(this.controller.containsKey(guildid)) {
			mc = this.controller.get(guildid);
		}else {
			mc = new MusicController(PixelBeat.INSTANCE.shardMan.getGuildById(guildid));
		
			this.controller.put(guildid, mc);
		}
		return mc;
	}
	
	public boolean clearController(long guildid) {	
		if(this.controller.containsKey(guildid)) {
			this.controller.remove(guildid);
			return true;
		}
		return false;
	}
	
	public long getGuildByPlayerHash(int hash) {
		for(MusicController controller : this.controller.values()) {
			if(controller.getPlayer().hashCode() == hash) {
				return controller.getGuild().getIdLong();
			}
		}
		
		return -1;
	}
	
}
