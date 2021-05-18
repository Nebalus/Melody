package de.melody.commands.types;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public interface DirectmessageCommand {

	public void performCommand(String command, User user, Message message);
	
}
