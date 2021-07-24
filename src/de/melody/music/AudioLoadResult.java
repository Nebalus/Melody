package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

public class AudioLoadResult implements AudioLoadResultHandler{

	private final MusicController controller;
	private final String uri;
	private final Member userWhoQueued;
	private final Boolean isPlaylist;
	private final Boolean isLoop;
	private final Boolean isLoopQueue;
	
	private MessageFormatter mf = Melody.INSTANCE.getMessageFormatter();
	
    public AudioLoadResult(MusicController controller,String uri,Member userWhoQueued,Boolean isPlaylist) {
    	this.controller = controller;
		this.uri = uri;
		this.userWhoQueued = userWhoQueued;
		this.isPlaylist = isPlaylist;
		this.isLoop = controller.isLoop();
		this.isLoopQueue = controller.isLoopQueue();
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		if(isLoop == false) {
			Queue queue = controller.getQueue();
			if(controller.isPlayingTrack()) {
				if(isPlaylist == false && isLoopQueue == false) {
					int QueueSize = queue.getQueueSize();
					QueueSize++;
					Long guildid = controller.getGuild().getIdLong();
					EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guildid, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
							.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
							.addField(mf.format(guildid, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
							.addField(mf.format(guildid, "music.track.position-in-queue"), QueueSize+"", true)
							.addField(mf.format(guildid, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
							
					if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
						String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
						builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
					}					
					MusicUtil.sendEmbled(controller.getGuild().getIdLong(), builder);
				}
			}
			queue.addTrackToQueue(track,userWhoQueued);	
		}else {
			controller.play(track);
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		Queue queue = controller.getQueue();
		Long guildid = controller.getGuild().getIdLong();
		if(isPlaylist == false) {
			if(uri.startsWith("ytsearch: ")) {
				if(controller.isPlayingTrack()) {
					int QueueSize = queue.getQueueSize();
					QueueSize++;
					AudioTrack track = playlist.getTracks().get(0);
					EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guildid, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
							.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
							.addField(mf.format(guildid, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
							.addField(mf.format(guildid, "music.track.position-in-queue"), QueueSize+"", true)
							.addField(mf.format(guildid, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
					
					if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
						String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
						builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
					}
					
					MusicUtil.sendEmbled(controller.getGuild().getIdLong(), builder);
					}	
				queue.addTrackToQueue(playlist.getTracks().get(0), userWhoQueued);	
			return;
		}
		if(playlist.getTracks().size() >= 1) {
			if(controller.isPlayingTrack()) {
				int QueueSize = queue.getQueueSize();
				QueueSize++;
				AudioTrack track = playlist.getTracks().get(0);
				EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guildid, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
						.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
						.addField(mf.format(guildid, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
						.addField(mf.format(guildid, "music.track.position-in-queue"), QueueSize+"", true)
						.addField(mf.format(guildid, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
				
				if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
					String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
					builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
				}
				
				MusicUtil.sendEmbled(controller.getGuild().getIdLong(), builder);
			}
			queue.addTrackToQueue(playlist.getTracks().get(0), userWhoQueued);	
			}	
		
	}else if(isPlaylist == true) {
		Long timeUntil = 0l;
		for(AudioTrack track : playlist.getTracks()) {
			queue.addTrackToQueue(track,userWhoQueued);
			timeUntil = timeUntil + track.getDuration();
		}
		if(playlist.getTracks().size() >= 1) {
			int QueueSize = queue.getQueueSize();
			QueueSize = QueueSize - playlist.getTracks().size() + 1;
			
			EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guildid, "music.playlist.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
					.setDescription("["+playlist.getName()+"]("+uri+")")
					.addField(mf.format(guildid, "music.track.position-in-queue"), (QueueSize == 0 ? "Now" : QueueSize+""), true)
					.addField(mf.format(guildid, "music.playlist.enqueued"), playlist.getTracks().size()+"", true)
					.addField(mf.format(guildid, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) - timeUntil == 0l ? "Now" : "In "+MusicUtil.getTime(null, MusicUtil.getTimeUntil(controller) - timeUntil)), true)
					.addField(mf.format(guildid, "music.playlist.length"), MusicUtil.getTime(null,timeUntil), false);
			
			MusicUtil.sendEmbled(controller.getGuild().getIdLong(), builder);
		}else {
				MusicUtil.sendEmbledError(controller.getGuild().getIdLong(), userWhoQueued.getAsMention()+ " "+mf.format(guildid, "music.playlist.empty"));
			}
		}
	}

	@Override
	public void noMatches() {
		Long guildid = controller.getGuild().getIdLong();
		if(uri.startsWith("ytsearch: ")) {
		EmbedBuilder builder = new EmbedBuilder()
				.setDescription(mf.format(guildid, "feedback.music.no-match"));	
		MusicUtil.sendEmbled(guildid, builder);
		}else {
			MusicUtil.sendEmbledError(guildid, mf.format(guildid, "feedback.music.nothing-in-link"));
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {}
}
