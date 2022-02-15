package de.melody.tools.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public final class TrackScheduler extends AudioEventAdapter {

	@Override
	public void onPlayerPause(AudioPlayer player) {
	
	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
	
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track_UNUSED) {

	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
	
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {

	}
}