package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
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
		this.queue = new Queue(this);
		this.afktime = 60;
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(Melody.INSTANCE.entityManager.getGuildEntity(guild.getIdLong()).getVolume());
		this.isloop = false;
		this.isloopqueue = false;
		
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
