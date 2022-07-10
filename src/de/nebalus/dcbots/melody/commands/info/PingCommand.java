package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.cmdbuilder.InternPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PingCommand extends ServerCommand
{

	public PingCommand() 
	{
		super("ping");
		setInternPermission(InternPermission.EVERYONE);
		setDescription("See the response time of " + Build.NAME + " to the Discord Gateway.");
	}
	
	@Override
	public void performMainCmd(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) 
	{
		final long gatewayping = channel.getJDA().getGatewayPing();
		channel.getJDA().getRestPing().queue((time) ->
			event.replyFormat(Melody.formatMessage(guild, "feedback.info.ping"), time, gatewayping).queue()
		);
	}
	
}