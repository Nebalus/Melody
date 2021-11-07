
package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.core.Melody;
import de.melody.speechpackets.MessageFormatter;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class AudioLoadResult implements AudioLoadResultHandler{

	private final MusicController controller;
	private final String uri;
	private final Member userWhoQueued;
	private final Boolean isLoop;
	private final Boolean isLoopQueue;
	private final Guild guild;
	private final String imageUrl;
	private final Service service;
	
	private MessageFormatter mf = Melody.INSTANCE.getMessageFormatter();
	
    public AudioLoadResult(MusicController controller, String uri, Service service) {
    	this.controller = controller;
		this.uri = uri;
		this.imageUrl = null;
		this.userWhoQueued = null;
		this.service = service;
		this.isLoop = controller.isLoop();
		this.isLoopQueue = controller.isLoopQueue();
		this.guild = controller.getGuild();
	}
    
    public AudioLoadResult(MusicController controller, String uri, Service service, Member userWhoQueued) {
    	this.controller = controller;
		this.uri = uri;
		this.imageUrl = null;
		this.userWhoQueued = userWhoQueued;
		this.service = service;
		this.isLoop = controller.isLoop();
		this.isLoopQueue = controller.isLoopQueue();
		this.guild = controller.getGuild();
	}
    
    public AudioLoadResult(MusicController controller, String uri, Service service, Member userWhoQueued, String imageUrl) {
    	this.controller = controller;
		this.uri = uri;
		this.imageUrl = imageUrl;
		this.userWhoQueued = userWhoQueued;
		this.service = service;
		this.isLoop = controller.isLoop();
		this.isLoopQueue = controller.isLoopQueue();
		this.guild = controller.getGuild();
	}
    
	@Override
	public void trackLoaded(AudioTrack track) {
		Queue queue = controller.getQueue();
		QueuedTrack queuedtrack = new QueuedTrack(track, userWhoQueued, service, imageUrl);
		if(controller.isPlayingTrack() && isLoop == false && isLoopQueue == false) {
			EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
					.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
					.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
					.addField(mf.format(guild, "music.track.position-in-queue"), queue.getQueueSize()+1+"", true)
					.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
			
			if(queuedtrack.getImageURL() != null) {
				builder.setThumbnail(queuedtrack.getImageURL());
			}
			
			MusicUtil.sendEmbled(guild, builder);
		}
		queue.addTrackToQueue(queuedtrack);	
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		Queue queue = controller.getQueue();
	
		if(uri.startsWith("ytsearch: ")) {
			QueuedTrack queuedtrack = new QueuedTrack(playlist.getTracks().get(0), userWhoQueued, service, imageUrl);
			if(controller.isPlayingTrack()) {
				AudioTrack track = playlist.getTracks().get(0);
				EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
						.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
						.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
						.addField(mf.format(guild, "music.track.position-in-queue"), queue.getQueueSize()+1+"", true)
						.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
				
				if(queuedtrack.getImageURL() != null) {
					builder.setThumbnail(queuedtrack.getImageURL());
				}
				MusicUtil.sendEmbled(guild, builder);
			}	
			queue.addTrackToQueue(queuedtrack);	
			return;
		}else {
			Long timeUntil = 0l;
			for(AudioTrack track : playlist.getTracks()) {
				queue.addTrackToQueue(new QueuedTrack(track, userWhoQueued, service));
				timeUntil = timeUntil + track.getDuration();
			}
			if(playlist.getTracks().size() >= 1) {
				final int QueueSize = queue.getQueueSize() - playlist.getTracks().size() + 1;
				
				EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.playlist.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
						.setDescription("["+playlist.getName()+"]("+uri+")")
						.addField(mf.format(guild, "music.track.position-in-queue"), (QueueSize == 0 ? "Now" : QueueSize+""), true)
						.addField(mf.format(guild, "music.playlist.enqueued"), playlist.getTracks().size()+"", true)
						.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) - timeUntil == 0l ? "Now" : "In "+MusicUtil.getTime(null, MusicUtil.getTimeUntil(controller) - timeUntil)), true)
						.addField(mf.format(guild, "music.playlist.length"), MusicUtil.getTime(null,timeUntil), false);
				
				MusicUtil.sendEmbled(guild, builder);
			}else {
				MusicUtil.sendEmbledError(guild, new ErrorMessageBuilder().setMessageFormat(guild, "music.playlist-empty"));
			}
		}
	}

	@Override
	public void noMatches() {
		if(uri.startsWith("ytsearch: ")) {
			MusicUtil.sendEmbledError(guild, new ErrorMessageBuilder().setMessageFormat(guild, "music.no-match"));
		}else {
			MusicUtil.sendEmbledError(guild, new ErrorMessageBuilder().setMessageFormat(guild, "music.nothing-in-link"));
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {}
}
