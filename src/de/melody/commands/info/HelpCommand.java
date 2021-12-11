package de.melody.commands.info;

import de.melody.entities.GuildEntity;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class HelpCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		// TODO Auto-generated method stub
	}
	
	private EmbedBuilder generateMenu(String searchquery) {
		EmbedBuilder builder = new EmbedBuilder();
		if(searchquery == null) {
			
		}
		return null;
	}

	@Override
	public String[] getCommandPrefix() {
		return new String[] {"help"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT;
	}

	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Shows the help menu";
	}

}
