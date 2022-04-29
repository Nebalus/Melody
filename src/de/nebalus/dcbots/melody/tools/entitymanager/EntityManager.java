package de.nebalus.dcbots.melody.tools.entitymanager;

import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;

public final class EntityManager {
	
	public ConcurrentHashMap<Long, GuildEntity> guildentity;
	
	public EntityManager() {
		this.guildentity = new ConcurrentHashMap<Long, GuildEntity>();
	}
	
	public GuildEntity getGuildEntity(Guild guild) {
		GuildEntity ge = null;
		Long guildid = guild.getIdLong();
		if(this.guildentity.containsKey(guildid)) {
			ge = this.guildentity.get(guildid);
		}else {
			ge = new GuildEntity(guildid);
			this.guildentity.put(guildid, ge);
		}
		return ge;
	}
	
	public void removeGuildEntity(GuildEntity ge) {
		if(ge.export()) {
			guildentity.remove(ge.getGuildId());
		}
	}
}
