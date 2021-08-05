package de.melody.commands.server.music;

import java.util.List;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicUtil;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.Emojis;
import de.melody.utils.Utils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class LeaveCommand implements ServerCommand{

	private Melody melody = Melody.INSTANCE;
	private MessageFormatter mf = melody.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		GuildVoiceState state;
		if((state = guild.getSelfMember().getVoiceState()) != null && state.getChannel() != null) {
			MusicUtil.MusicKiller(guild);
			message.addReaction(Emojis.OK_HAND).queue();
		}else 
			Utils.sendErrorEmbled(channel, mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"), m);
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("leave","l");
	}
}
