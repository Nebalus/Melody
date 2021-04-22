package de.pixelbeat.music;

import java.util.HashMap;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.pixelbeat.PixelBeat;
import de.pixelbeat.entities.QueueEmbed;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

	private Guild guild;
	private AudioPlayer player;
	private Queue queue;
	private HashMap<Long, QueueEmbed> queueembed;
	
	public MusicController(Guild guild) {
		this.guild = guild;
		this.player = PixelBeat.INSTANCE.audioPlayerManager.createPlayer();
		this.queue = new Queue(this);
		
		this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
		this.player.addListener(new TrackScheduler());
		this.player.setVolume(MusicUtil.getVolume(guild.getIdLong()));
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
	
	public QueueEmbed getQueueEmbed(Long messageid) {
		if(queueembed.containsKey(messageid)) {
			return queueembed.get(messageid);
		}
		return null;
	}
	
	public void addQueueEmbed(Long messageid, QueueEmbed queueEmbed) {
		if(!queueembed.containsKey(messageid)) {
			queueembed.put(messageid, queueEmbed);
		}
	}
	
}
