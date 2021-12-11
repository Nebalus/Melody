package de.melody.commands.info;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class InviteCommand implements ServerCommand{

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {
		Messenger.sendMessage(channel,Melody.INSTANCE.getMessageFormatter().format(channel.getGuild(), "feedback.info.invite", Constants.INVITE_URL)).queue();
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		event.reply(Melody.INSTANCE.getMessageFormatter().format(guild, "feedback.info.invite", Constants.INVITE_URL)).queue();
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"invite"};
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public String getCommandDescription() {
		return "Invite "+Constants.BUILDNAME+" to your own Discord Server";
	}
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
}
