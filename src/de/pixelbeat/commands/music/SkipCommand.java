package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.ConsoleLogger;
import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.Queue;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SkipCommand implements ServerCommand{


	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		long guild = m.getGuild().getIdLong();
		GuildVoiceState state;
		if((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				MusicUtil.updateChannel(channel);
				if(args.length == 1) {
					try {
						AudioPlayer player = controller.getPlayer();
						Queue queue = controller.getQueue();
						if(player.getPlayingTrack() != null) {
							player.stopTrack();
							if(queue.nextexist()) {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setDescription(Emojis.NEXT_TITLE+" Track skipped");
								MusicUtil.sendEmbled(channel.getGuild().getIdLong(), builder);
								queue.next();
							}else {
								EmbedBuilder builder = new EmbedBuilder();
								builder.setDescription(Emojis.NEXT_TITLE+" Track skipped, could not find the next track");
								MusicUtil.sendEmbled(channel.getGuild().getIdLong(), builder);
							}
						}else {
							EmbedBuilder builder = new EmbedBuilder();
							builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" Currently i am not playing a track");
							MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
						}
					}catch(NumberFormatException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
