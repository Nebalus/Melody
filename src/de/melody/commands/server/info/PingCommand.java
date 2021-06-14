package de.melody.commands.server.info;

import de.melody.Melody;
import de.melody.commands.types.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PingCommand implements SlashCommand{

	@Override
	public CommandData getCommandData() {
		return new CommandData("ping", "Checks "+Melody.name+"'s response time to Discord");
	}

	@Override
	public void performSlashCommand(SlashCommandEvent slash) {
		long gatewayping = slash.getJDA().getGatewayPing();
		
		slash.getJDA().getRestPing().queue( (time) ->
			slash.replyFormat(Melody.INSTANCE.getMessageFormatter().format(slash.getGuild().getIdLong(), "feedback.info.ping"), time, gatewayping).queue()
		);
		
	}

}
