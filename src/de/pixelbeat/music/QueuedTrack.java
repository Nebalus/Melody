package de.pixelbeat.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;

public class QueuedTrack {

	private AudioTrack track;
	private Member whoQueued;
	
	public QueuedTrack(AudioTrack track, Member whoQueued) {
		this.track = track;
		this.whoQueued = whoQueued;
	}
	
	public Member getWhoQueued() {
		return whoQueued;
	}
	
	public AudioTrack getTrack() {
		return track;
	}
}
