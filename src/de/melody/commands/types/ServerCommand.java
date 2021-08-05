package de.melody.commands.types;

import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public interface ServerCommand {

	public void performCommand(Member m, TextChannel channel, Message message, Guild guild);
	
	public List<String> getCommandPrefix();
	//The first String in the list is the main command prefix and will be used in the SlashCommand Sektion. 
	
	//public String getCommandDescription();
}

