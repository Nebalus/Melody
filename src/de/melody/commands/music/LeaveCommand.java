package de.melody.commands.music;

import java.util.List;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.commandbuilder.CommandInfo;
import de.melody.utils.commandbuilder.CommandType;
import de.melody.utils.commandbuilder.ServerCommand;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class LeaveCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member member, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicUtil.MusicKiller(guild);
			Messenger.sendMessage(channel, mf.format(guild, "music.info.bot-leave-vc", state.getChannel().getName()));
		}else {
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicUtil.MusicKiller(guild);
			event.reply(mf.format(guild, "music.info.bot-leave-vc", state.getChannel().getName()));
		}else { 
			Messenger.sendErrorMessage(channel, new ErrorMessageBuilder().setMessageFormat(guild, "music.bot-not-in-vc"));
		}
	}
	
	@Override
	public List<String> getCommandPrefix() {
		return List.of("leave","l");
	}
	
	@Override
	public CommandType getCommandType() {
		return CommandType.BOTH;
	}

	@Override
	public CommandInfo getCommandInfo() {
		return CommandInfo.DJ_COMMAND;
	}
	
	@Override
	public String getCommandDescription() {
		return "Let "+Constants.BUILDNAME+" leave your voice channel";
	}

	@Override
	public List<OptionData> getCommandOptions() {
		return null;
	}
}
