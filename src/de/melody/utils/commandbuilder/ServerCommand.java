package de.melody.utils.commandbuilder;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public interface ServerCommand {

	public void performCommand(Member member, TextChannel channel, Message message, Guild guild);
	
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event);
	
	public List<String> getCommandPrefix();
	
	public CommandType getCommandType();
	
	public CommandInfo getCommandInfo();
	
	public List<OptionData> getCommandOptions();
	
	public String getCommandDescription();
}

