package de.melody.commands.server.info;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) 	{		
		long gatewayping = channel.getJDA().getGatewayPing();
	
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(Melody.INSTANCE.getMessageFormatter().format(channel.getGuild().getIdLong(), "feedback.info.ping"), time, gatewayping).queue()
		);
	}
}
