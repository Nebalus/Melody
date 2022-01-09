package de.melody.commands.slash;

import de.melody.core.Melody;
import de.melody.entities.GuildEntity;
import de.melody.speechpackets.MessageFormatter;
import de.melody.tools.commandbuilder.Command;
import de.melody.tools.commandbuilder.CommandPermission;
import de.melody.tools.commandbuilder.CommandType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class PrefixCommand implements Command{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody._messageformatter;
	
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
		return "Gets the current prefix";
	}
	@Override
	public OptionData[] getCommandOptions() {
		return null;
	}
	@Override
	public CommandPermission getMainPermmision() {
		return CommandPermission.EVERYONE;
	}
}
