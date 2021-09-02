package de.melody.commands.server.slash;

import java.util.List;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class PrefixCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		event.reply(mf.format(guild, "feedback.info.prefix",melody.entityManager.getGuildEntity(guild).getPrefix())).queue();
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("prefix");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.SLASH_COMMAND;
	}

	@Override
	public boolean isSlashCommandCompatible() {
		return true;
	}
	
	@Override
	public String getCommandDescription() {
		return "Gets the current prefix from "+Constants.BUILDNAME;
	}
}
