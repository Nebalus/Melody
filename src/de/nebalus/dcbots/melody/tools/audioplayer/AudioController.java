package de.nebalus.dcbots.melody.tools.audioplayer;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.core.Melody;
import de.nebalus.dcbots.melody.core.constants.Settings;
import de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

public final class AudioController 
{
	
	private final AudioPlayer player;
	private final Queue queue;
	private final long guildid;
	
	private long timeouttime = System.currentTimeMillis() + Settings.MUSIC_AFK_DEFAULT;
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
	
	public void join(AudioChannel ac)
	{
		Melody.getGuildById(guildid).getAudioManager().openAudioConnection(ac);
		if(player.getPlayingTrack() == null) 
		{
			updateTimeOutTime(TimeUnit.MINUTES, 5);
		}
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
	
	public long getTimeOutTime()
	{
		return this.timeouttime;
	}
	
	public void updateTimeOutTime(TimeUnit timeunit, long duration) 
	{
		this.timeouttime = System.currentTimeMillis() + timeunit.toMillis(duration);
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
