package de.melody.commands.types;

import java.util.List;

import de.melody.CommandManager.CommandType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public interface ServerCommand {

	public void performCommand(Member m, TextChannel channel, Message message, Guild guild);
	
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event);
	
	public List<String> getCommandPrefix();
	
	public CommandType getCommandType();
	
	public boolean isSlashCommandCompatible();
	
	//The first String in the list is the main command prefix and will be used in the SlashCommand Sektion. 
	public String getCommandDescription();
}

