package de.nebalus.dcbots.melody.audioplayer.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.nebalus.dcbots.melody.MelodyCore;
import de.nebalus.dcbots.melody.audioplayer.GuildAudioController;

public class AudioLoadResult implements AudioLoadResultHandler {

	public GuildAudioController controller;
	
	public AudioLoadResult(GuildAudioController controller) {
		this.controller = controller;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		MelodyCore.getMelodyApp().getLogger().log("trackLoaded");
		controller.getPlayer().destroy();
		controller.getPlayer().playTrack(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		MelodyCore.getMelodyApp().getLogger().log("playlistLoaded");
	}

	@Override
	public void noMatches() {
		MelodyCore.getMelodyApp().getLogger().log("noMatches ");
	}

	@Override
	public void loadFailed(FriendlyException exception) {
		MelodyCore.getMelodyApp().getLogger().logError(exception);
	}
}
