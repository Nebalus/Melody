package de.nebalus.dcbots.melody.audioplayer;

import java.util.Arrays;

import com.github.natanbc.lavadsp.rotation.RotationPcmAudioFilter;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.nebalus.dcbots.melody.MelodyBotInstance;
import de.nebalus.dcbots.melody.audioplayer.handler.AudioPlayerSendHandler;
import de.nebalus.dcbots.melody.audioplayer.queue.TrackQueue;
import net.dv8tion.jda.api.entities.Guild;

public class GuildAudioController {
	
	private final AudioPlayer player;
	private final TrackQueue trackQueue;
	private final long guildId;
	
	public GuildAudioController(MelodyBotInstance botInstance, Guild guild) {
		player = botInstance.getAudioPlayerManager().createPlayer();
		player.addListener(new TrackScheduler());
		player.setVolume(50);
		player.setPaused(false);
		
		trackQueue = new TrackQueue(this); 
		guildId = guild.getIdLong();
		
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
	
		player.setFilterFactory(((track, format, output) ->{
			RotationPcmAudioFilter rotation = new RotationPcmAudioFilter(output, format.sampleRate);
			rotation.setRotationSpeed(0.25D);
			return Arrays.asList(rotation);
		}));
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public TrackQueue getTrackQueue() {
		return trackQueue;
	}
	
	public long getGuildId() {
		return guildId;
	}
}
