package de.pixelbeat.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private int afktime;
	
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = PixelBeat.INSTANCE.audioPlayerManager.createPlayer();
		this.queue = new Queue(this);
		this.afktime = -1;
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(PixelBeat.INSTANCE.entityManager.getGuildEntity(guild.getIdLong()).getVolume());
	}
	
	public Guild getGuild() {
		return guild;
	}
	
	public AudioPlayer getPlayer() {
		return player;
	}
	
	public Queue getQueue() {
		return queue;
	}
	
	public int getAfkTime() {
		return afktime;
	}
	
	public void setAfkTime(int time) {
		afktime = time;
	}
	
}
