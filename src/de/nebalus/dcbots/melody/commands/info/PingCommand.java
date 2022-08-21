package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import de.nebalus.dcbots.melody.tools.cmdbuilder.SlashExecuter;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class PingCommand extends SlashCommand
{

	public PingCommand() 
	{
		super("ping");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("See the response time of " + Build.NAME + " to the Discord Gateway.");
		
		setExecuter(new SlashExecuter()
		{
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event, InteractionHook hook) 
			{
				final long gatewayping = channel.getJDA().getGatewayPing();
				channel.getJDA().getRestPing().queue((time) ->
					hook.sendMessageFormat(Melody.formatMessage(guild, "feedback.info.ping"), time, gatewayping).queue()
				);
			}
		});
	}
	
}