package de.pixelbeat.commands.music;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class PauseCommand implements ServerCommand{

	private PixelBeat pixelbeat = PixelBeat.INSTANCE;
	private MessageFormatter mf = pixelbeat.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		pixelbeat.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(channel.getIdLong());

		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getGuild().getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			pixelbeat.playerManager.getController(guild.getIdLong()).getPlayer().setPaused(true);
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"));
		}
	}
}
