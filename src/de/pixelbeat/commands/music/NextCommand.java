package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.Queue;
import de.pixelbeat.speechpackets.MessageFormatter;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class NextCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@SuppressWarnings("unused")
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicUtil.updateChannel(channel);
		GuildVoiceState state;
		VoiceChannel vc;
		EmbedBuilder builder = new EmbedBuilder();
		if((state = guild.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guild.getIdLong());
			String[] args = message.getContentDisplay().split(" ");
			if(args.length == 1) {
				try {
					AudioPlayer player = controller.getPlayer();
					Queue queue = controller.getQueue();
					if(player.getPlayingTrack() != null) {
						player.stopTrack();
						if(queue.nextexist()) {
							builder.setDescription(Emojis.NEXT_TITLE+" "+mf.format(guild.getIdLong(), "music.track.skip"));
							MusicUtil.sendEmbled(guild.getIdLong(), builder);
							queue.next();
						}else {
							builder.setDescription(Emojis.NEXT_TITLE+" "+mf.format(guild.getIdLong(), "music.track.skip-new-null"));
							MusicUtil.sendEmbled(guild.getIdLong(), builder);
						}
					}else {
						MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "music.info.currently-playing-null"));
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "feedback.music.bot-not-in-vc"));
		}
	}
}
