package de.melody.entities;

import java.util.concurrent.ConcurrentHashMap;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class EntityManager {

	public ConcurrentHashMap<Long, UserEntity> userentity;
	public ConcurrentHashMap<Long, GuildEntity> guildentity;
	public ConcurrentHashMap<Integer, PlaylistEntity> playlistentity;
	
	public EntityManager() {
		this.userentity = new ConcurrentHashMap<Long, UserEntity>();
		this.guildentity = new ConcurrentHashMap<Long, GuildEntity>();
		this.playlistentity = new ConcurrentHashMap<Integer, PlaylistEntity>();
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
		if(ue.export()) {
			userentity.remove(ue.getUserId());
		}
	}
	
	public GuildEntity getGuildEntity(Guild guild) {
		GuildEntity ge = null;
		Long guildid = guild.getIdLong();
		if(this.guildentity.containsKey(guildid)) {
			ge = this.guildentity.get(guildid);
		}else {
			ge = new GuildEntity(guild);
			this.guildentity.put(guildid, ge);
		}
		return ge;
	}
	
	public void removeGuildEntity(GuildEntity ge) {
		if(ge.export()) {
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
