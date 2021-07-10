package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.melody.Melody;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private int afktime;
	
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = Melody.INSTANCE.audioPlayerManager.createPlayer();
		this.queue = new Queue(this);
		this.afktime = 20;
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(Melody.INSTANCE.entityManager.getGuildEntity(guild.getIdLong()).getVolume());
		/*
		 * Funktioniert nicht auf einen raspberry pi
		this.player.setFilterFactory((track, format, output)->{
			GuildEntity ge = PixelBeat.INSTANCE.entityManager.getGuildEntity(guild.getIdLong());
			TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
		    timescale.setSpeed(ge.getSpeed());
		    return Arrays.asList(timescale);
		});
		*/
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public Queue getQueue() {
		return queue;
	}
	
	public int getAfkTime() {
		return afktime;
	}
	
	public void setAfkTime(int time) {
		afktime = time;
	}
	
}
