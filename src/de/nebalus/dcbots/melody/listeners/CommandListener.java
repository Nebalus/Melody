package de.nebalus.dcbots.melody.listeners;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandManager;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.EntityManager;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;

public final class CommandListener extends ListenerAdapter 
{
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		final CommandManager commandmanager = Melody.getCommandManager();
		final EntityManager entitymanager = Melody.getEntityManager();
		final String cmdprefix = event.getName().toLowerCase();
		final String cmdpath = event.getCommandPath();
		final InteractionHook hook = event.getHook();
		
		final SlashCommand cmd;
		
		try 
		{
			cmd = commandmanager.getCommand(cmdprefix);
		} 
		catch (NullPointerException e)
		{
			e.printStackTrace();
			hook.sendMessage("This command is currently not available.").setEphemeral(true).queue();
			return;
		}

		final SlashExecuter executer;
		
		final String[] subcmdrelations = cmdpath.split("/");
		
		switch(subcmdrelations.length)
		{
			case 1:
				executer = cmd.getExecuter();
				break;
			
			case 2:
			case 3:
				executer = cmd.getSubCommandByPath(cmdpath.substring(cmdprefix.length() + 1)).getExecuter();
				break;
			
			default:
				hook.sendMessage("The Executable from the CMDPATH:" + cmdpath + " could not be found!!!").setEphemeral(true).queue();
				return;
		}
		
		
		
		
		if (event.isFromGuild()) 
		{
			Guild guild = event.getGuild();
			GuildEntity ge = entitymanager.getGuildEntity(guild);
			if(!ge.isRateLimited()) 
			{
				ge.addRateRequest();
				try 
				{
			    	if(!commandmanager.performSlashGuild(executer, cmd.getPermissionGroup(), ge, event)) 
			    	{	
			    		hook.sendMessage("This command is currently not available.").setEphemeral(true).queue();
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
					Messenger.sendErrorMessage(event, "ratelimit", Melody.getConfig().RATELIMITREQUEST, Melody.getConfig().RATELIMITITERATIONDURATION);
				}
			}
	    }
		else if(!cmd.isGuildOnly())
	    {
	    	
	    }
		else
		{
			hook.sendMessage("Hmm Something was not setup correctly!!!").setEphemeral(true).queue();
			return;
		}
	}
}