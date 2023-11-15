package de.nebalus.dcbots.melody.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.nebalus.dcbots.melody.MelodyCore;

public class TrackScheduler extends AudioEventAdapter {

	@Override
	public void onPlayerPause(AudioPlayer player) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Player Paused");
    }

	@Override
    public void onPlayerResume(AudioPlayer player) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Player Resume");
    }

	@Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Track Start");
    }

	@Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Track End");
    }

	@Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Track Exception");
    }

	@Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		MelodyCore.getMelodyApp().getLogger().logDebug("Track Stuck");
    }
}
