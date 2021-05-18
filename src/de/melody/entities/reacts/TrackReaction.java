package de.melody.entities.reacts;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.entities.reacts.ReactionManager.ReactionRaw;

public class TrackReaction implements ReactionRaw{

	private AudioTrackInfo track;
	
	public TrackReaction(AudioTrackInfo track) {
		this.track = track;
	}
	
	public String getTitle() {
		return track.title;
	}
	
	public String getUrl() {
		return track.uri;
	}
	
	@Override
	public ReactionTypes getType() {
		return ReactionTypes.TRACKREACTION;
	}
}
