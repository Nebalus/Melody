package de.nebalus.dcbots.melody.commands.info;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Build;
import de.nebalus.dcbots.melody.core.constants.Url;
import de.nebalus.dcbots.melody.tools.cmdbuilder.CommandPermission;
import de.nebalus.dcbots.melody.tools.cmdbuilder.ServerCommand;
import de.nebalus.dcbots.melody.tools.entitymanager.entitys.GuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class InviteCommand extends ServerCommand{

	public InviteCommand() {
		super("invite");
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("Get an Invite link to invite " + Build.NAME + " to your own Discord Server.");
	}
	
	@Override
	public void performMainCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandInteractionEvent event) {
		event.reply(Melody.formatMessage(guild, "feedback.info.invite", Url.INVITE.toString())).queue();
	}
}