package de.melody.commands.slash;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class PrefixCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild, GuildEntity guildentity) {}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, GuildEntity guildentity, SlashCommandEvent event) {
		event.reply(mf.format(guild, "feedback.info.prefix",guildentity.getPrefix())).queue();
	}
	
	@Override
	public String[] getCommandPrefix() {
		return new String[] {"prefix"};
	}

	@Override
	public CommandType getCommandType() {
		return CommandType.SLASH;
	}

	@Override
	public String getCommandDescription() {
		return "Gets the current prefix from "+Constants.BUILDNAME;
	}
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
}
