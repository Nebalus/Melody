package de.nebalus.dcbots.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.core.Constants;
import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.tools.audioplayer.audioloader.AudioPlayerSendHandler;
import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import net.dv8tion.jda.api.entities.Guild;

public final class AudioController {
	
	private final AudioPlayer player;
	private final Queue queue;
	private int timeouttime = Constants.MUSIK_AFK_DEFAULT;
	private LoopMode loopmode = LoopMode.NONE;
	private Long anouncechannelid;
	
	public AudioController(Guild guild) {
		this.player = Melody.INSTANCE.audioPlayerMan.createPlayer();
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.queue = new Queue(this);
		
		this.player.setPaused(false);
		this.player.setVolume(50);
		this.player.addListener(new TrackScheduler());
	}
	
	public Boolean isPlayingTrack() {
		if(player.getPlayingTrack() == null) {
			return false;
		}
		return true;	
	}
	
	public void play(AudioTrack at) {
		this.player.stopTrack();
		this.player.playTrack(at);
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public void setAnounceChannelId(Long anouncechannelid) {
		this.anouncechannelid = anouncechannelid;
	}
	
	public Long getAnounceChannelId() {
		return anouncechannelid;
	}
	
	public LoopMode getLoopMode() {
		return loopmode;
	}
	
	public void setLoopMode(LoopMode loopmode) {
		this.loopmode = loopmode;
	}
	
	public int getTimeOutTime() {
		return timeouttime;
	}
	
	public void setTimeOutTime(int time) {
		timeouttime = time;
	}
	
	public Queue getQueue() {
		return queue;
	}
}
