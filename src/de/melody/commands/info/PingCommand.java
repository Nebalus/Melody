package de.melody.commands.info;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class PingCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {		
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(mf.format(channel.getGuild(), "feedback.info.ping"), time, gatewayping).queue()
		);
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			event.replyFormat(mf.format(guild, "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"ping", "latency"};
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}


	@Override
	public String getCommandDescription() {
		return "See the response time of "+Constants.BUILDNAME+" to the Discord Gateway";
	}
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
}
