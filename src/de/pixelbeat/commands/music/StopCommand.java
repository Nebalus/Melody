package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.Queue;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class StopCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		GuildVoiceState state;
		MusicUtil.updateChannel(channel);
		if((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				MusicController controller = PixelBeat.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				AudioPlayer player = controller.getPlayer();
				if(player.getPlayingTrack() != null) {
					Queue queue = controller.getQueue();
					player.stopTrack();
					queue.clearall();
					MusicUtil.getVoiceAfkTime.put(vc.getGuild().getIdLong(), 600l);
					message.addReaction("U+1F44C").queue();
				}
			}
		}	
	}
}
