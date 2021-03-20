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

public class PauseCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicUtil.updateChannel(channel);

		GuildVoiceState state;
		if((state = m.getGuild().getSelfMember().getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				AudioPlayer player = controller.getPlayer();
				player.setPaused(true);
						
			}else {
				EmbedBuilder builder = new EmbedBuilder();
				builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" I am not in a voice channel.");
				MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
					}
		}else {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" I am not in a voice channel.");
			MusicUtil.sendEmbledError(channel.getGuild().getIdLong(), builder);
		}
	}
}
