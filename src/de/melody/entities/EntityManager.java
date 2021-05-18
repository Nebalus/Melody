package de.melody.entities;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import de.melody.Melody;
import net.dv8tion.jda.api.entities.User;

public class EntityManager {

	public ConcurrentHashMap<Long, GuildEntityController> guildcontroller;
	public HashMap<Long, UserEntity> userentity;
	public HashMap<Long, GuildEntity> guildentity;
	public HashMap<Integer, PlaylistEntity> playlistentity;
	
	public EntityManager() {
		this.guildcontroller = new ConcurrentHashMap<Long, GuildEntityController>();
		this.userentity = new HashMap<Long, UserEntity>();
		this.guildentity = new HashMap<Long, GuildEntity>();
		this.playlistentity = new HashMap<Integer, PlaylistEntity>();
	}
	
	public GuildEntityController getGuildController(long guildid) {
		GuildEntityController ec = null;
		
		if(this.guildcontroller.containsKey(guildid)) {
			ec = this.guildcontroller.get(guildid);
		}else {
			ec = new GuildEntityController(Melody.INSTANCE.shardMan.getGuildById(guildid));
			this.guildcontroller.put(guildid, ec);
		}
		return ec;
	}
	
	public UserEntity getUserEntity(User user) {
		UserEntity ue = null;
		Long userid = user.getIdLong();
		if(this.userentity.containsKey(userid)) {
			ue = this.userentity.get(userid);
		}else {
			ue = new UserEntity(user);
			this.userentity.put(userid, ue);
		}
		return ue;
	}
	
	public void removeUserEntity(UserEntity ue) {
		if(ue.exportData()) {
			userentity.remove(ue.getUserId());
		}
	}
	
	public GuildEntity getGuildEntity(Long guildid) {
		GuildEntity ge = null;
		if(this.guildentity.containsKey(guildid)) {
			ge = this.guildentity.get(guildid);
		}else {
			ge = new GuildEntity(guildid);
			this.guildentity.put(guildid, ge);
		}
		return ge;
	}
	
	public void removeGuildEntity(GuildEntity ge) {
		if(ge.exportData()) {
			guildentity.remove(ge.getGuildId());
		}
	}
	
	public PlaylistEntity getPlaylistEntity(int playlistid) {
		PlaylistEntity pe = null;
		if(this.playlistentity.containsKey(playlistid)) {
			pe = this.playlistentity.get(playlistid);
		}else {
			pe = new PlaylistEntity(playlistid);
			this.playlistentity.put(playlistid, pe);
		}
		return pe;
	}
}