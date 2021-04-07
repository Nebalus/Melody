package de.pixelbeat.music;

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
	    this.buffer = ByteBuffer.allocate(1024);
	    this.frame = new MutableAudioFrame();
	    this.frame.setBuffer(buffer);
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
	  
	  /*
	  
	  alte instance
	  
	  private final AudioPlayer audioPlayer;
	  private AudioFrame lastFrame;

	  public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
	    this.audioPlayer = audioPlayer;
	  }

	  @Override
	  public boolean canProvide() {
	    lastFrame = audioPlayer.provide();
	    return lastFrame != null;
	  }

	  @Override
	  public ByteBuffer provide20MsAudio() {
	    return ByteBuffer.wrap(lastFrame.getData());
	  }

	  @Override
	  public boolean isOpus() {
	    return true;
	  }
	}
	*/
}
