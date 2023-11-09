package old.de.nebalus.dcbots.melody.tools.audioplayer;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.core.constants.Settings;
import old.de.nebalus.dcbots.melody.tools.audioplayer.enums.LoopMode;

public final class AudioController {

	private final AudioPlayer player;
	private final Queue queue;
	private final long guildid;

	private long timeouttime = System.currentTimeMillis() + Settings.MUSIC_AFK_DEFAULT;
	private LoopMode loopmode = LoopMode.NONE;
	private Long anouncechannelid;

	public AudioController(Guild guild) {
		player = Melody.getAudioPlayerManager().createPlayer();
		guildid = guild.getIdLong();
		queue = new Queue(this);

		guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));

		player.setPaused(false);
		player.setVolume(50);
		player.addListener(new TrackScheduler());
	}

	public Boolean isPlayingTrack() {
		return player.getPlayingTrack() != null;
	}

	public void play(AudioTrack at) {
		player.stopTrack();
		player.playTrack(at);
	}

	public void join(AudioChannel ac) {
		Melody.getGuildById(guildid).getAudioManager().openAudioConnection(ac);
		if (player.getPlayingTrack() == null) {
			updateTimeOutTime(TimeUnit.MINUTES, 5);
		}
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

	public long getTimeOutTime() {
		return timeouttime;
	}

	public void updateTimeOutTime(TimeUnit timeunit, long duration) {
		timeouttime = System.currentTimeMillis() + timeunit.toMillis(duration);
	}

	public Queue getQueue() {
		return queue;
	}

	public long getGuildId() {
		return guildid;
	}
}
