package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class LoopCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicUtil.updateChannel(channel);

		GuildVoiceState state;
		VoiceChannel vc;
		if((state = m.getVoiceState()) != null && (vc = state.getChannel()) != null) {
			MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guild.getIdLong());
			AudioPlayer player = controller.getPlayer();
			if(player.getPlayingTrack() != null) {
				if(controller.getQueue().isLoop()) {
					controller.getQueue().setLoop(false);
					channel.sendMessage(Emojis.SINGLE_LOOP+mf.format(guild.getIdLong(), "music.info.loop-disabled")).queue();
				}else {
					controller.getQueue().setLoop(true);
					channel.sendMessage(Emojis.SINGLE_LOOP+mf.format(guild.getIdLong(), "music.info.loop-enabled")).queue();
				}	
			}else {
				MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "music.info.currently-playing-null"));
			}				
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"));
		}
	}
}