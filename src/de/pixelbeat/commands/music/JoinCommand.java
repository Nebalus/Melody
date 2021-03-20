package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ServerCommand{
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		GuildVoiceState state;
		MusicUtil.updateChannel(channel);
		if((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				AudioManager manager = vc.getGuild().getAudioManager();
				manager.openAudioConnection(vc);
				MusicController controller = PixelBeat.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
				AudioPlayer player = controller.getPlayer();
				player.setPaused(false);
				if(player.getPlayingTrack() == null) {
					MusicUtil.getVoiceAfkTime.put(vc.getGuild().getIdLong(), 600l);
				}
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" You are probably not in a voice channel.");
				MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
			}
		}
		else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" You are probably not in a voice channel.");
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);		
		}
	}
}
