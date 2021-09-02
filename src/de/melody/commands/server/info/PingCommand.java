package de.melody.commands.server.info;

import java.util.List;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class PingCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) 	{		
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(Melody.INSTANCE.getMessageFormatter().format(channel.getGuild(), "feedback.info.ping"), time, gatewayping).queue()
		);
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			event.replyFormat(Melody.INSTANCE.getMessageFormatter().format(guild, "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("ping");
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.INFO_COMMAND;
	}
	
	@Override
	public boolean isSlashCommandCompatible() {
		return true;
	}
	
	@Override
	public String getCommandDescription() {
		return "See the response time of "+Constants.BUILDNAME+" to the Discord Gateway";
	}
}
