package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Constants;
import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class PingCommand extends ServerCommand{

	public PingCommand() {
		super();
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("See the response time of "+Constants.BUILDNAME+" to the Discord Gateway.");
		setPrefix("ping");
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(Melody.formatMessage(guild, "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
}