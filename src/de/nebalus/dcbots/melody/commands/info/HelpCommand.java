package de.nebalus.dcbots.melody.commands.info;

import java.util.ArrayList;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.InternPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.DefaultEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class HelpCommand extends ServerCommand
{
	
	public HelpCommand() 
	{
		super("help");
		setInternPermission(InternPermission.EVERYONE);
		setDescription("Shows the help menu.");
		setSlashCommandData(
				Commands.slash(getPrefix(), getDescription())
					.addOption(OptionType.STRING, "query", "Enter the name of an Command to get more information", false)
		);
	}
	
	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		if(event.getOption("query") != null) 
		{
			final String searchquery = event.getOption("query").getAsString();
			event.replyEmbeds(generateMenu(searchquery).build()).setEphemeral(true).queue();
		}
		else
		{
			event.replyEmbeds(generateMenu(null).build()).setEphemeral(true).queue();
		}
	}
	
	private EmbedBuilder generateMenu(String searchquery) 
	{
		final DefaultEmbedBuilder builder = new DefaultEmbedBuilder();
		
		if(searchquery == null) 
		{
			builder.setAuthor("Help Command", null, Url.ICON.toString());
			
			final ArrayList<String> admincmds = new ArrayList<String>();
			final ArrayList<String> djcmds = new ArrayList<String>();
			final ArrayList<String> everyonecmds = new ArrayList<String>();
			
			for(ServerCommand scmd : Melody.getCommandManager().getCommands()) 
			{
				switch(scmd.getInternPermission()) 
				{
					case ADMIN:
						admincmds.add("`"+scmd.getPrefix()+"`");
						break;
						
					case DJ:
						djcmds.add("`"+scmd.getPrefix()+"`");
						break;
						
					case EVERYONE:
						everyonecmds.add("`"+scmd.getPrefix()+"`");
						break;
					
					default:
						break;
				}
			}
			
			if(!admincmds.isEmpty()) 
			{
				builder.addField("**Admin Commands**", admincmds.toString().replace("[", "").replace("]", ""), false);
			}
			
			if(!djcmds.isEmpty()) 
			{
				builder.addField("**DJ Commands**", djcmds.toString().replace("[", "").replace("]", ""), false);
			}
			
			if(!everyonecmds.isEmpty()) 
			{
				builder.addField("**Everyone Commands**", everyonecmds.toString().replace("[", "").replace("]", ""), false);
			}
			
			builder.addField("**Command Dashboard**", "[View Commands](" + Url.COMMANDS.toString() + "?p=" + Settings.CMD_PREFIX + ")", false);
			builder.setFooter("Type '" + Settings.CMD_PREFIX + "help <CommandName>' for details on a command.");
			
		}
		else
		{
			ServerCommand cmd;
			if((cmd = Melody.getCommandManager().getCommand(searchquery)) != null)
			{
				builder.setAuthor("Help Command: "+searchquery, null, Url.ICON.toString());
				String description;
				
				builder.setDescription("This feature is still work and progress");				
			}
			else
			{
				return generateMenu(null);
			}
			
		}
		return builder;
	}
}