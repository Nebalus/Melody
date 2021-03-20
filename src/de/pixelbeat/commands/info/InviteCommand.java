package de.pixelbeat.commands.info;

import de.pixelbeat.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class InviteCommand implements ServerCommand{

	
	public void performCommand(Member m, TextChannel channel, Message message) {
		channel.sendMessage(m.getAsMention() +" here is my my invite link which you can add me to your Discord server:\n"
				+ "\n"
				+ "Invitelink: https://discord.com/oauth2/authorize?client_id=801856678063898644&scope=bot&permissions=8").queue();
	}
}
