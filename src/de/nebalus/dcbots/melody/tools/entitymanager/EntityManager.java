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
	
	public EntityManager() 
	{
		this.guildentity = new ConcurrentHashMap<Long, GuildEntity>();
		this.userentity = new ConcurrentHashMap<Long, UserEntity>();
	}
	
	public GuildEntity getGuildEntity(Guild guild) 
	{
		return getGuildEntity(guild.getIdLong());
	}
	
	public GuildEntity getGuildEntity(Long guildid) 
	{
		GuildEntity ge = null;
		if(this.guildentity.containsKey(guildid)) 
		{
			ge = this.guildentity.get(guildid);
		}
		else 
		{
			ge = new GuildEntity(guildid);
			this.guildentity.put(guildid, ge);
		}
		return ge;
	}
	
	public UserEntity getUserEntity(User user) 
	{
		return getUserEntity(user.getIdLong());
	}
	
	public UserEntity getUserEntity(Long userid) 
	{
		UserEntity ue = null;
		if(this.userentity.containsKey(userid)) 
		{
			ue = this.userentity.get(userid);
		}
		else 
		{
			ue = new UserEntity(userid);
			this.userentity.put(userid, ue);
		}
		return ue;
	}
	
	private boolean removeGuildEntity(GuildEntity ge) 
	{
		if(ge.export()) 
		{
			guildentity.remove(ge.getGuildId());
			return true;
		}
		return false;
	}
	
	private boolean removeUserEntity(UserEntity ue) 
	{
//		if(ue.export()) 
//		{
//			guildentity.remove(ue.getUserId());
//			return true;
//		}
		return false;
	}
	
	public boolean exportToDatabase() 
	{
		boolean exported = false;
		
		for(Entry<Long, GuildEntity> guildentry : guildentity.entrySet()) 
		{
			GuildEntity value = guildentry.getValue();
			if(value.isExpired()) 
			{
				if(removeGuildEntity(value)) 
				{
					ConsoleLogger.debug("NEED TO EXPORT 1");	
					exported = true;	
				}
			}
			else if(value.needToExport()) 
			{
				if(value.export()) 
				{
					ConsoleLogger.debug("NEED TO EXPORT 2");	
					exported = true;	
				}
			}
		}
		
		for(Entry<Long, UserEntity> userentry : userentity.entrySet()) 
		{
			UserEntity value = userentry.getValue();
			if(value.isExpired()) 
			{
				if(removeUserEntity(value)) 
				{
					ConsoleLogger.debug("NEED TO EXPORT 3");	
					exported = true;	
				}
			}
			else if(value.needToExport()) 
			{
				ConsoleLogger.debug("NEED TO EXPORT 4");	
				//value.export();
				exported = true;
			}
		}
		
		return exported;
	}
}
