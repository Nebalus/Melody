package de.nebalus.dcbots.melody.tools.audioplayer;

import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.core.Melody;

public final class MusicManager 
{
	private ConcurrentHashMap<Long, AudioController> controller;
	
	public MusicManager()
	{
		this.controller = new ConcurrentHashMap<Long, AudioController>();
	}
	
	public AudioController getController(long guildid) 
	{
		if(this.controller.containsKey(guildid)) 
		{
			AudioController ac = this.controller.get(guildid);
			return ac;
		}
		else 
		{
			AudioController ac = new AudioController(Melody.getGuildById(guildid));
			this.controller.put(guildid, ac);
			return ac;
		}
	}
	
	public void clearController(long guildid) 
	{	
		if(this.controller.containsKey(guildid)) 
		{
			this.controller.remove(guildid);
		}
	}
	
	public long getGuildIdByPlayerHash(int hash) 
	{
		for(AudioController controller : this.controller.values()) 
		{
			if(controller.getPlayer().hashCode() == hash) 
			{
				return controller.getGuildId();
			}
		}	
		return -1;
	}
}
