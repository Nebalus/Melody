package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.music.Queue;
import de.pixelbeat.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StopCommand implements ServerCommand{

	private MessageFormatter mf = PixelBeat.INSTANCE.getMessageFormatter();
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		MusicUtil.updateChannel(channel);
		MusicController controller = PixelBeat.INSTANCE.playerManager.getController(guild.getIdLong());
		AudioPlayer player = controller.getPlayer();
		if(player.getPlayingTrack() != null) {
			Queue queue = controller.getQueue();
			player.stopTrack();
			queue.clearall();
			controller.setAfkTime(600);
			message.addReaction("U+1F44C").queue();
		}else {
			MusicUtil.sendEmbledError(guild.getIdLong(), mf.format(guild.getIdLong(), "music.info.currently-playing-null"));
		}	
	}
}
