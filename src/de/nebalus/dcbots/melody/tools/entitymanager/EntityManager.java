package de.nebalus.dcbots.melody.tools.entitymanager;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import de.nebalus.dcbots.melody.tools.ConsoleLogger;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public final class EntityManager {

	private final ConcurrentHashMap<Long, GuildEntity> guildentity;
	private final ConcurrentHashMap<Long, UserEntity> userentity;

	public EntityManager() {
		guildentity = new ConcurrentHashMap<>();
		userentity = new ConcurrentHashMap<>();
	}

	public GuildEntity getGuildEntity(Guild guild) {
		return getGuildEntity(guild.getIdLong());
	}

	public GuildEntity getGuildEntity(Long guildid) {
		GuildEntity ge = null;
		if (guildentity.containsKey(guildid)) {
			ge = guildentity.get(guildid);
		} else {
			ge = new GuildEntity(guildid);
			guildentity.put(guildid, ge);
		}
		return ge;
	}

	public UserEntity getUserEntity(User user) {
		return getUserEntity(user.getIdLong());
	}

	public UserEntity getUserEntity(Long userid) {
		UserEntity ue = null;
		if (userentity.containsKey(userid)) {
			ue = userentity.get(userid);
		} else {
			ue = new UserEntity(userid);
			userentity.put(userid, ue);
		}
		return ue;
	}

	private boolean removeGuildEntity(GuildEntity ge) {
		if (ge.export()) {
			guildentity.remove(ge.getGuildId());
			return true;
		}
		return false;
	}

	private boolean removeUserEntity(UserEntity ue) {
//		if(ue.export())
//		{
//			guildentity.remove(ue.getUserId());
//			return true;
//		}
		return false;
	}

	public boolean exportToDatabase() {
		boolean exported = false;

		for (Entry<Long, GuildEntity> guildentry : guildentity.entrySet()) {
			GuildEntity value = guildentry.getValue();
			if (value.isExpired()) {
				if (removeGuildEntity(value)) {
					ConsoleLogger.debug("NEED TO EXPORT 1");
					exported = true;
				}
			} else if (value.needToExport()) {
				if (value.export()) {
					ConsoleLogger.debug("NEED TO EXPORT 2");
					exported = true;
				}
			}
		}

		for (Entry<Long, UserEntity> userentry : userentity.entrySet()) {
			UserEntity value = userentry.getValue();
			if (value.isExpired()) {
				if (removeUserEntity(value)) {
					ConsoleLogger.debug("NEED TO EXPORT 3");
					exported = true;
				}
			} else if (value.needToExport()) {
				ConsoleLogger.debug("NEED TO EXPORT 4");
				// value.export();
				exported = true;
			}
		}

		return exported;
	}
}
