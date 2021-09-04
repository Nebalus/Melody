package de.melody.commands.server.music;

import java.util.List;

import de.melody.CommandManager.CommandType;
import de.melody.commands.types.ServerCommand;
import de.melody.core.Melody;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emoji;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;


public class LeaveCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicUtil.MusicKiller(guild);
			message.addReaction(Emoji.OK_HAND).queue();
		}else 
			Utils.sendErrorEmbled(message, mf.format(guild, "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("leave","l");
	}
	@Override
	public CommandType getCommandType() {
		return CommandType.MUSIC_COMMAND;
	}
	@Override
	public boolean isSlashCommandCompatible() {
		return false;
	}
	@Override
	public String getCommandDescription() {
		return null;
	}

	@Override
	public void performSlashCommand(Member member, MessageChannel channel, Guild guild, SlashCommandEvent event) {
		
	}
}
