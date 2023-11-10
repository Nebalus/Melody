package old.de.nebalus.dcbots.melody.interactions.commands.admin;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import old.de.nebalus.dcbots.melody.core.constants.Build;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Messenger;

public class StaymodeCommand extends SlashCommand {

	public StaymodeCommand() {
		super("247");
		setDescription("Toggles " + Build.NAME + " to stay 24/7 in a voicechat.");

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity,
					SlashCommandInteractionEvent event) {
				if (guildentity.is24_7()) {
					guildentity.set24_7(false);
					Messenger.sendInteractionMessage(event,
							Melody.formatMessage(guild, "command.admin.staymode.disabled"), false);
				} else {
					guildentity.set24_7(true);
					Messenger.sendInteractionMessage(event,
							Melody.formatMessage(guild, "command.admin.staymode.enabled"), false);
				}
			}
		});
	}
}
