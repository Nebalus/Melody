package de.melody.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Member;

public class QueuedTrack {

	private final AudioTrack track;
	private final Member whoQueued;
	private final String imageUrl;
	private final Service service;
	
	public QueuedTrack(AudioTrack track, Member whoQueued, Service service) {
		this.track = track;
		this.whoQueued = whoQueued;
		this.imageUrl = MusicUtil.getTrackImageURL(track.getInfo().uri);
		this.service = service;
	}
	
	public QueuedTrack(AudioTrack track, Member whoQueued, Service service, String imageUrl) {
		this.track = track;
		this.whoQueued = whoQueued;
		if(imageUrl == null) {
			this.imageUrl = MusicUtil.getTrackImageURL(track.getInfo().uri);
		}else {
			this.imageUrl = imageUrl;
		}
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
	
	public String getImageURL() {
		return imageUrl;
	}
}
