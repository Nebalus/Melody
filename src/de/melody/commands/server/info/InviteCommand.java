package de.melody.commands.server.info;

import java.util.List;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class InviteCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		channel.sendMessage(Melody.INSTANCE.getMessageFormatter().format(channel.getGuild(), "feedback.info.invite", m.getAsMention(), Constants.INVITE_URL)).queue();
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		channel.sendMessage(Melody.INSTANCE.getMessageFormatter().format(guild, "feedback.info.invite", member.getAsMention(), Constants.INVITE_URL)).queue();
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("invite");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.INFO_COMMAND;
	}
	@Override
	public boolean isSlashCommandCompatible() {
		return true;
	}
	@Override
	public String getCommandDescription() {
		return "Invite "+Constants.BUILDNAME+" to your own Discord Server";
	}
}
