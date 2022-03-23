package de.melody.commands.info;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.cmdbuilder.CommandPermission;
import de.melody.tools.cmdbuilder.CommandType;
import de.melody.tools.cmdbuilder.ServerCommand;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PingCommand extends ServerCommand{

	public PingCommand() {
		super();
		setType(CommandType.BOTH);
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("See the response time of "+Constants.BUILDNAME+" to the Discord Gateway.");
		setPrefixes("ping");
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue( (time) ->
			channel.sendMessageFormat(Melody.formatMessage(channel.getGuild(), "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
}
