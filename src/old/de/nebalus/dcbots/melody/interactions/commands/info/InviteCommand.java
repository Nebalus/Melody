package old.de.nebalus.dcbots.melody.interactions.commands.info;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import old.de.nebalus.dcbots.melody.core.constants.Build;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.core.constants.Url;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.PermissionGroup;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.SlashCommand;
import old.de.nebalus.dcbots.melody.tools.cmdbuilder.interactions.SlashInteractionExecuter;
import old.de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import old.de.nebalus.dcbots.melody.tools.messenger.Messenger;

public class InviteCommand extends SlashCommand {

	public InviteCommand() {
		super("invite");
		setPermissionGroup(PermissionGroup.EVERYONE);
		setDescription("Shows the invitation link to invite " + Build.NAME + " to your own Discord server.");

		setExecuter(new SlashInteractionExecuter() {
			@Override
			public void executeGuild(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
				Messenger.sendInteractionMessage(event, Melody.formatMessage(guild, "command.info.invite", Url.INVITE.toString()), true);
			}
		});
	}

}