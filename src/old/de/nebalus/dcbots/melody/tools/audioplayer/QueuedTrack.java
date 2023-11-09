package old.de.nebalus.dcbots.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.User;
import old.de.nebalus.dcbots.melody.core.constants.Melody;
import old.de.nebalus.dcbots.melody.tools.audioplayer.enums.Service;

public final class QueuedTrack {

	private AudioTrack track;
	private final Long whoQueuedId;
	private final Service service;

	public QueuedTrack(AudioTrack track, Long whoQueuedId, Service service) {
		this.track = track;
		this.whoQueuedId = whoQueuedId;
		this.service = service;
	}

	public Service getService() {
		return service;
	}

	public User getWhoQueued() {
		return Melody.getUserById(whoQueuedId);
	}

	public AudioTrack getTrack() {
		return track;
	}

	public AudioTrack refreshTrack() {
		track = track.makeClone();
		return track;
	}
}