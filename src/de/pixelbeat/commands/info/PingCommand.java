package de.pixelbeat.commands.info;

import de.pixelbeat.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ServerCommand{

	
	public void performCommand(Member m, TextChannel channel, Message message) {
		long gatewayping = channel.getJDA().getGatewayPing();
	
		channel.getJDA().getRestPing().queue( (time) ->
		channel.sendMessageFormat("The latency of the bot is %dm"+"s", time, gatewayping).queue()
			
		);
	}
}
