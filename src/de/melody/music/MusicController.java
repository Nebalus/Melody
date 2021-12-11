package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.music.audioloader.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private int afktime;
	private boolean isloop;	
	private boolean isloopqueue;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = Melody.INSTANCE.audioPlayerManager.createPlayer();
		this.player.setPaused(false);
		this.queue = new Queue(this);
		this.afktime = Constants.MUSIK_AFK_DEFAULT;
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(Melody.INSTANCE.getEntityManager().getGuildEntity(guild).getVolume());
		this.isloop = false;
		this.isloopqueue = false;
		
		/*
		this.player.setFilterFactory((track, format, output)->{
			TimescalePcmAudioFilter timescale = new TimescalePcmAudioFilter(output, format.channelCount, format.sampleRate);
		    timescale.setSpeed(1.1);
		    timescale.setPitch(1.2);
		    return Arrays.asList(timescale);
		});
		*/
		
	}
	
	public Boolean isPlayingTrack() {
		if(player.getPlayingTrack() == null) {
			return false;
		}
		return true;	
	}
	
	public void play(AudioTrack at) {
		this.player.destroy();
		this.player.playTrack(at);
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
	
	public boolean isLoop() {
		return isloop;
	}
	
	public boolean isLoopQueue() {
		return isloopqueue;
	}
	
	public void setLoopQueue(Boolean loopqueue) {
		this.isloopqueue = loopqueue;
	}
	
	public void setLoop(Boolean loop) {
		this.isloop = loop;
	}	
}
