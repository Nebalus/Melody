package de.pixelbeat.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.commands.types.ServerCommand;
import de.pixelbeat.music.MusicController;
import de.pixelbeat.music.MusicUtil;
import de.pixelbeat.utils.Images;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TrackinfoCommand implements ServerCommand{

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		MusicUtil.updateChannel(channel);
		EmbedBuilder builder = new EmbedBuilder();
		builder.setImage("attachment://trackinfo.png");
		try {
			MusicController controller = PixelBeat.INSTANCE.playerManager.getController(channel.getGuild().getIdLong());
			AudioTrack audiotrack = controller.getPlayer().getPlayingTrack();
			channel.sendFile( Images.tracktojpg(audiotrack.getInfo().title,audiotrack.getPosition(),audiotrack.getDuration(),audiotrack.getInfo().author), "trackinfo.png").embed(builder.build()).queue();	
		}catch (NullPointerException e) {
			channel.sendFile( Images.tracktojpg(null,0,0,null), "trackinfo.png").embed(builder.build()).queue();	
		}
	}
}
