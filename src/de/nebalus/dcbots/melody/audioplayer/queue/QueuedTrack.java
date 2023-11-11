package de.nebalus.dcbots.melody.audioplayer.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.audioplayer.enums.Service;

public class QueuedTrack {

	private AudioTrack audioTrack;
	private Long requestorId;
	private Service service;

	public QueuedTrack(AudioTrack audioTrack, Long requestorId, Service service) {
		this.audioTrack = audioTrack;
		this.requestorId = requestorId;
		this.service = service;
	}
	
	public AudioTrack getTrack() {
		return audioTrack;
	}
	
	public Long getRequestorId() {
		return requestorId;
	}
	
	public Service getService() {
		return service;
	}
}
