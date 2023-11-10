package de.nebalus.dcbots.melody.audioplayer.queue;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class QueuedTrack {

	private AudioTrack audioTrack;
	private Long userRequestorId;

	public QueuedTrack(AudioTrack audioTrack, Long userRequestorId) {
		this.audioTrack = audioTrack;
		this.userRequestorId = userRequestorId;
	}
	
}
