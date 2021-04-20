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
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class NextCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicUtil.updateChannel(channel);
		GuildVoiceState state;
		VoiceChannel vc;
		EmbedBuilder builder = new EmbedBuilder();
		if((state = m.getGuild().getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
			String[] args = message.getContentDisplay().split(" ");
			if(args.length == 1) {
				try {
					AudioPlayer player = controller.getPlayer();
					Queue queue = controller.getQueue();
					if(player.getPlayingTrack() != null) {
						player.stopTrack();
						if(queue.nextexist()) {
							builder.setDescription(Emojis.NEXT_TITLE+" Track skipped");
							MusicUtil.sendEmbled(channel.getGuild().getIdLong(), builder);
							queue.next();
						}else {
							builder.setDescription(Emojis.NEXT_TITLE+" Track skipped, could not find the next track");
							MusicUtil.sendEmbled(channel.getGuild().getIdLong(), builder);
						}
					}else {
						builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+mf.format(channel.getGuild().getIdLong(), "music.info.currently-playing-null"));
						MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
					}
				}catch(NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}else {
			builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" "+mf.format(channel.getGuild().getIdLong(), "feedback.music.bot-not-in-vc"));
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
		}
	}
}
