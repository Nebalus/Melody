package de.nebalus.dcbots.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import net.dv8tion.jda.api.entities.Guild;

public final class AudioController 
{
	
	private final AudioPlayer player;
	private final Queue queue;
	private final long guildid;
	
	private int timeouttime = Settings.MUSIC_AFK_DEFAULT;
	private LoopMode loopmode = LoopMode.NONE;
	private Long anouncechannelid;
	
	public AudioController(Guild guild) 
	{
		this.player = Melody.getAudioPlayerManager().createPlayer();
		this.guildid = guild.getIdLong();
		this.queue = new Queue(this);
		
		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		
		this.player.setPaused(false);
		this.player.setVolume(50);
		this.player.addListener(new TrackScheduler());
	}
	
	public Boolean isPlayingTrack() 
	{
		return player.getPlayingTrack() != null;	
	}
	
	public void play(AudioTrack at) 
	{
		this.player.stopTrack();
		this.player.playTrack(at);
	}
	
	public AudioPlayer getPlayer() 
	{
		return this.player;
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
		return this.queue;
	}
	
	public long getGuildId()
	{
		return this.guildid;
	}
}
