package de.melody.commands.server.info;

import java.util.List;


import de.melody.core.Config;
import de.melody.core.Melody;
import de.nebalus.botbuilder.command.CommandInfo;
import de.nebalus.botbuilder.command.CommandType;
import de.nebalus.botbuilder.command.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class InviteCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		channel.sendMessage(Melody.INSTANCE.getMessageFormatter().format(channel.getGuild(), "feedback.info.invite", m.getAsMention(), Config.INVITE_URL)).queue();
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		event.reply(Melody.INSTANCE.getMessageFormatter().format(guild, "feedback.info.invite", member.getAsMention(), Config.INVITE_URL)).queue();
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("invite");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}
	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.INFO_COMMAND;
	}
	@Override
	public String getCommandDescription() {
		return "Invite "+Config.BUILDNAME+" to your own Discord Server";
	}
	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}
