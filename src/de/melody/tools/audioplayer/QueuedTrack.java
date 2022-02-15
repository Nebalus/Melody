package de.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.tools.audioplayer.enums.Service;
import net.dv8tion.jda.api.entities.Member;

public final class QueuedTrack {

	private AudioTrack track;
	private final Member whoQueued;
	private final Service service;
	
	public QueuedTrack(AudioTrack track, Member whoQueued, Service service) {
		this.track = track;
		this.whoQueued = whoQueued;
		this.service = service;
	}

	public Service getService() {
		return service;
	}
	
	public Member getWhoQueued() {
		return whoQueued;
	}
	
	public AudioTrack getTrack() {
		return track;
	}
	
	public void refreshTrack() {
		track = track.makeClone();
	}
}