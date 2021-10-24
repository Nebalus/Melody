package de.melody.commands.dev;

import java.util.List;

import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class ExportcmdCommand implements ServerCommand {

	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("exportcommands");
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.CHAT_COMMAND;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.DEVELOPER_COMMAND;
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}

	@Override
	public String getCommandDescription() {
		return "Exports all commands in a HTML format";
	}

}
