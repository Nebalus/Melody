package de.pixelbeat.commands.music;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LeaveCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicUtil.updateChannel(channel);
		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getGuild().getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
		
			MusicUtil.MusicKiller(vc.getGuild());
			message.addReaction("U+1F44C").queue();
		}else {
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), mf.format(channel.getGuild().getIdLong(), "feedback.music.bot-not-in-vc"));
		}
	}
}
