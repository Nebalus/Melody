package de.pixelbeat.commands.server.info;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) 	{		
		long gatewayping = channel.getJDA().getGatewayPing();
	
		channel.getJDA().getRestPing().queue( (time) ->
		channel.sendMessageFormat(PixelBeat.INSTANCE.getMessageFormatter().format(channel.getGuild().getIdLong(), "feedback.info.ping"), time, gatewayping).queue()
		);
	}
}
