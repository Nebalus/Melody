package de.nebalus.dcbots.melody.commands.info;

import java.util.ArrayList;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import de.nebalus.dcbots.melody.tools.messenger.embedbuilders.DefaultEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand extends SlashCommand
{
	
	public HelpCommand() 
	{
		super("help");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows the help menu.");
		
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook)
			{
				if(event.getOption("query") != null) 
				{
					final String searchquery = event.getOption("query").getAsString();
					hook.sendMessageEmbeds(generateMenu(searchquery).build()).setEphemeral(true).queue();
				}
				else
				{
					hook.sendMessageEmbeds(generateMenu(null).build()).setEphemeral(true).queue();
				}
			}
		});
		
		addOption(new OptionData(OptionType.STRING, "query", "Enter the name of an Command to get more information")
			.setRequired(false));
			
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
			
			for(SlashCommand scmd : Melody.getCommandManager().getCommands()) 
			{
				switch(scmd.getPermissionGroup()) 
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
			SlashCommand cmd;
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