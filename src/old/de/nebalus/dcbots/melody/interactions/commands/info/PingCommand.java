package old.de.nebalus.dcbots.melody.interactions.commands.info;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import old.de.nebalus.dcbots.melody.core.constants.Build;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Messenger;

public class PingCommand extends SlashCommand {

	public PingCommand() {
		super("ping");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows the latency of " + Build.NAME + " to Discord's gateway.");

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
					SlashCommandInteractionEvent event) {
				final long gatewayping = channel.getJDA().getGatewayPing();
				channel.getJDA().getRestPing().queue((time) -> Messenger.sendInteractionMessageFormat(event,
						Melody.formatMessage(guild, "command.info.ping"), true, time, gatewayping));
			}
		});
	}

}