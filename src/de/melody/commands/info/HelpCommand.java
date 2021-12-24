package de.melody.commands.info;

import java.util.ArrayList;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.utils.commandbuilder.CommandPermission;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class HelpCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessageEmbed(channel, generateMenu(null, guildentity)).queue();
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		
	}
	
	private EmbedBuilder generateMenu(String searchquery, GuildEntity guildentity) {
		EmbedBuilder builder = new EmbedBuilder();
		if(searchquery == null) {
			builder.setAuthor("Help Command", null, Constants.ICON_URL);
			
			ArrayList<String> admincmds = new ArrayList<String>();
			ArrayList<String> djcmds = new ArrayList<String>();
			ArrayList<String> everyonecmds = new ArrayList<String>();
			
			for(ServerCommand scmd : Melody.INSTANCE.getCmdMan().getRawCommands()) {
				switch(scmd.getMainPermmision()) {
					case ADMIN:
						admincmds.add("`"+scmd.getCommandPrefix()[0]+"`");
						break;
						
					case DJ:
						djcmds.add("`"+scmd.getCommandPrefix()[0]+"`");
						break;
						
					case EVERYONE:
						everyonecmds.add("`"+scmd.getCommandPrefix()[0]+"`");
						break;
					
					default:
						break;
				}
			}
			builder.addField("**Admin Commands**", admincmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**DJ Commands**", djcmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**Everyone Commands**", everyonecmds.toString().replace("[", "").replace("]", ""), false);
			builder.addField("**Web Dashboard**", "[View Commands]("+Constants.COMMAND_URL+")", false);
			//builder.setFooter("Type '"+guildentity.getPrefix()+"help <CommandName>' for details on a command.");
		}
		return builder;
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"help", "h"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		return new OptionData[] {new OptionData(OptionType.STRING, "command", "Enter a command name")};
	}

	@Override
	public String getCommandDescription() {
		return "Shows the help menu";
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}
}
