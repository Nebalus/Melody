package de.nebalus.dcbots.melody.audioplayer.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.audioplayer.enums.StreamingService;

public class QueuedTrack {

	private AudioTrack audioTrack;
	private Long requestorId;
	private StreamingService streamingService;

	public QueuedTrack(AudioTrack audioTrack, Long requestorId, StreamingService streamingService) {
		this.audioTrack = audioTrack;
		this.requestorId = requestorId;
		this.streamingService = streamingService;
	}
	
	public AudioTrack getTrack() {
		return audioTrack;
	}
	
	public Long getRequestorId() {
		return requestorId;
	}
	
	public StreamingService getService() {
		return streamingService;
	}
}
