package de.melody.commands.server.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.commands.types.ServerCommand;
import de.melody.music.MusicController;
import de.melody.utils.Images;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TrackinfoCommand implements ServerCommand{
	
	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message, Guild guild) {
		melody.entityManager.getGuildEntity(guild.getIdLong()).setChannelId(channel.getIdLong());
		try {
			MusicController controller = Melody.INSTANCE.playerManager.getController(guild.getIdLong());
			AudioTrack audiotrack = controller.getPlayer().getPlayingTrack();
			channel.sendFile(Images.tracktopng(audiotrack.getInfo().title,audiotrack.getPosition(),audiotrack.getDuration(),audiotrack.getInfo().author,guild.getIdLong(),controller.getQueue().getCurrentPlaying().getWhoQueued()), "trackinfo.png").queue();	
		}catch (NullPointerException e) {
			channel.sendFile(Images.tracktopng(null,0,0,null,guild.getIdLong(),null), "trackinfo.png").queue();	
		}
	}

	@Override
	public List<String> getCommandPrefix() {
		return List.of("trackinfo","ti");
	}
}
