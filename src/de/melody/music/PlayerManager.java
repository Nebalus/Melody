package de.melody.music;

import java.util.concurrent.ConcurrentHashMap;

import de.melody.core.Melody;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;


public class PlayerManager {

	public ConcurrentHashMap<Long, MusicController> controller;
	public ConcurrentHashMap<Long, Long> anouncechannel;
	
	public PlayerManager() {
		this.controller = new ConcurrentHashMap<Long, MusicController>();
		this.anouncechannel = new ConcurrentHashMap<Long, Long>();
	}
	
	public MusicController getController(long guildid) {
		MusicController mc = null;		
		if(this.controller.containsKey(guildid)) {
			mc = this.controller.get(guildid);
		}else {
			mc = new MusicController(Melody.INSTANCE._shardMan.getGuildById(guildid));
			this.controller.put(guildid, mc);
		}
		return mc;
	}
	
	public void clearController(long guildid) {	
		if(this.controller.containsKey(guildid)) {
			this.controller.remove(guildid);
		}
		if(this.anouncechannel.containsKey(guildid)) {
			this.anouncechannel.remove(guildid);
		}
	}
	
	public long getGuildByPlayerHash(int hash) {
		for(MusicController controller : this.controller.values()) {
			if(controller.getPlayer().hashCode() == hash) {
				return controller.getGuild().getIdLong();
			}
		}	
		return -1;
	}
	
	public void setAnounceChannelID(Long guildid, Long anouncechannelid) {
		if(this.anouncechannel.containsKey(guildid)) {
			anouncechannel.remove(guildid);
		}
		anouncechannel.put(guildid, anouncechannelid);
	}
	
	public TextChannel getAnounceChannel(Guild guild) {
		if(this.anouncechannel.containsKey(guild.getIdLong())) {
			TextChannel channel;
			if((channel = guild.getTextChannelById(anouncechannel.get(guild.getIdLong()))) != null) {
				return channel;
			}
			channel = guild.getTextChannels().get(0);
			setAnounceChannelID(guild.getIdLong(), channel.getIdLong());
			return channel;
		}
		boolean mentioned = false;
		for(TextChannel tc : guild.getTextChannels()) {
			if(!mentioned) {
				try {
					mentioned = true;
					setAnounceChannelID(guild.getIdLong(), tc.getIdLong());
					return tc;
				}catch(InsufficientPermissionException e) {}
			}
		}
		return null;
	}
}
