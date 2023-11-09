package old.de.nebalus.dcbots.melody.tools.audioplayer;

import java.nio.ByteBuffer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import net.dv8tion.jda.api.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

	private final AudioPlayer audioPlayer;
	private final ByteBuffer buffer;
	private final MutableAudioFrame frame;

	public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
		this.audioPlayer = audioPlayer;
		buffer = ByteBuffer.allocate(2048);
		frame = new MutableAudioFrame();
		frame.setBuffer(buffer);
	}

	@Override
	public boolean canProvide() {
		return audioPlayer.provide(frame);
	}

	@Override
	public ByteBuffer provide20MsAudio() {
		return buffer.flip();
	}

	@Override
	public boolean isOpus() {
		return true;
	}
}
