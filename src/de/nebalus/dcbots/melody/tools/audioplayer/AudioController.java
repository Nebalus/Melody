package de.nebalus.dcbots.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import net.dv8tion.jda.api.entities.Guild;

public final class AudioController 
{
	
	private final AudioPlayer PLAYER;
	private final Queue QUEUE;
	private final long GUILDID;
	
	private int timeouttime = Settings.MUSIC_AFK_DEFAULT;
	private LoopMode loopmode = LoopMode.NONE;
	private Long anouncechannelid;
	
	public AudioController(Guild guild) 
	{
		this.PLAYER = Melody.INSTANCE.audioplayerMan.createPlayer();
		this.GUILDID = guild.getIdLong();
		
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(PLAYER));
		this.QUEUE = new Queue(this);
		
		this.PLAYER.setPaused(false);
		this.PLAYER.setVolume(50);
		this.PLAYER.addListener(new TrackScheduler());
	}
	
	public Boolean isPlayingTrack() 
	{
		return PLAYER.getPlayingTrack() != null;	
	}
	
	public void play(AudioTrack at) 
	{
		this.PLAYER.stopTrack();
		this.PLAYER.playTrack(at);
	}
	
	public AudioPlayer getPlayer() 
	{
		return this.PLAYER;
	}
	
	public void setAnounceChannelId(Long anouncechannelid) 
	{
		this.anouncechannelid = anouncechannelid;
	}
	
	public Long getAnounceChannelId()
	{
		return anouncechannelid;
	}
	
	public LoopMode getLoopMode() 
	{
		return this.loopmode;
	}
	
	public void setLoopMode(LoopMode loopmode) 
	{
		this.loopmode = loopmode;
	}
	
	public int getTimeOutTime()
	{
		return this.timeouttime;
	}
	
	public void setTimeOutTime(int timeouttime) 
	{
		this.timeouttime = timeouttime;
	}
	
	public Queue getQueue() 
	{
		return this.QUEUE;
	}
	
	public long getGuildId()
	{
		return this.GUILDID;
	}
}
