package de.nebalus.dcbots.melody.listeners;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class CommandListener extends ListenerAdapter 
{
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		if (event.isFromGuild()) 
		{
			Guild guild = event.getGuild();
			GuildEntity ge = Melody.getEntityManager().getGuildEntity(guild);
			if(!ge.isRateLimited()) 
			{
				ge.addRateRequest();
				try 
				{
			    	if(!Melody.getCommandManager().performServer(ge, event)) 
			    	{	
			    		event.reply("This command is currently not available.").setEphemeral(true).queue();
					}
				}
				catch(InsufficientPermissionException e) 
				{
					Messenger.sendErrorMessage(event, "bot-no-permmisions", e.getPermission());
				}
			}
			else 
			{
				if(!ge.ratelimitmsgsend) 
				{
					ge.ratelimitmsgsend = true;
					Messenger.sendErrorMessage(event, "info.ratelimit", Melody.getConfig()._ratelimitmaxrequests, Melody.getConfig()._ratelimititerationduration);
				}
			}
	    }
		else 
	    {
	    	event.reply("My commands only work in a guild where im in :( \n" + Url.INVITE.toString()).setEphemeral(true).queue();
	    }
	}
}