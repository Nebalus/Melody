package de.melody.commands.info;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.tools.cmdbuilder.CommandPermission;
import de.melody.tools.cmdbuilder.CommandType;
import de.melody.tools.cmdbuilder.ServerCommand;
import de.melody.tools.entitymanager.entitys.GuildEntity;
import de.melody.tools.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class InviteCommand extends ServerCommand{

	public InviteCommand() {
		super();
		setType(CommandType.BOTH);
		setMainPermission(CommandPermission.EVERYONE);
		setDescription("Get an Invite link to invite " + Constants.BUILDNAME + " to your own Discord Server.");
		setPrefixes("invite");
	}
	
	@Override
	public void performMainCMD(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessage(channel, Melody.formatMessage(guild, "feedback.info.invite", Constants.INVITE_URL)).queue();
	}
	
	@Override
	public void performMainSlashCMD(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		Messenger.sendMessage(event, Melody.formatMessage(guild, "feedback.info.invite", Constants.INVITE_URL)).queue();
	}
}