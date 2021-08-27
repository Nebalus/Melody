
package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import de.melody.Melody;
import de.melody.speechpackets.MessageFormatter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public class AudioLoadResult implements AudioLoadResultHandler{

	private final MusicController controller;
	private final String uri;
	private final Member userWhoQueued;
	private final Boolean isPlaylist;
	private final Boolean isLoop;
	private final Boolean isLoopQueue;
	private final Guild guild;
	
	private MessageFormatter mf = Melody.INSTANCE.getMessageFormatter();
	
    public AudioLoadResult(MusicController controller,String uri,Member userWhoQueued,Boolean isPlaylist) {
    	this.controller = controller;
		this.uri = uri;
		this.userWhoQueued = userWhoQueued;
		this.isPlaylist = isPlaylist;
		this.isLoop = controller.isLoop();
		this.isLoopQueue = controller.isLoopQueue();
		this.guild = controller.getGuild();
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		Queue queue = controller.getQueue();
		if(controller.isPlayingTrack() && isLoop == false && isPlaylist == false && isLoopQueue == false) {
			EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
					.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
					.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
					.addField(mf.format(guild, "music.track.position-in-queue"), queue.getQueueSize()+1+"", true)
					.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
							
			if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
				builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
			}					
			MusicUtil.sendEmbled(guild, builder);
		}
		queue.addTrackToQueue(track,userWhoQueued);	
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		Queue queue = controller.getQueue();
		if(isPlaylist) {
			Long timeUntil = 0l;
			for(AudioTrack track : playlist.getTracks()) {
				queue.addTrackToQueue(track,userWhoQueued);
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
				MusicUtil.sendEmbledError(guild, userWhoQueued.getAsMention()+ " "+mf.format(guild, "music.playlist.empty"));
			}
		}else {
			if(uri.startsWith("ytsearch: ")) {
				if(controller.isPlayingTrack()) {
					AudioTrack track = playlist.getTracks().get(0);
					EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
							.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
							.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
							.addField(mf.format(guild, "music.track.position-in-queue"), queue.getQueueSize()+1+"", true)
							.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
					
					if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
						String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
						builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
					}
					
					MusicUtil.sendEmbled(guild, builder);
				}	
				queue.addTrackToQueue(playlist.getTracks().get(0), userWhoQueued);	
				return;
			}
			if(playlist.getTracks().size() >= 1) {
				if(controller.isPlayingTrack()) {
					AudioTrack track = playlist.getTracks().get(0);
					EmbedBuilder builder = new EmbedBuilder().setAuthor(mf.format(guild, "music.track.added-to-queue"), null, userWhoQueued.getUser().getAvatarUrl())
							.setDescription("["+track.getInfo().title+"]("+track.getInfo().uri+")")
							.addField(mf.format(guild, "music.track.length"), MusicUtil.getTime(track.getInfo(),0l) , true)
							.addField(mf.format(guild, "music.track.position-in-queue"), queue.getQueueSize()+1+"", true)
							.addField(mf.format(guild, "music.track.time-until-playing"),  (MusicUtil.getTimeUntil(controller) == 0l ? "Now" : "In "+MusicUtil.getTime(null,MusicUtil.getTimeUntil(controller))), true);
					
					if(track.getInfo().uri.startsWith("https://www.youtube.com/watch?v=")) {
						String videoID = track.getInfo().uri.replace("https://www.youtube.com/watch?v=", "");
						builder.setThumbnail("https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp");
					}
					
					MusicUtil.sendEmbled(guild, builder);
				}
			queue.addTrackToQueue(playlist.getTracks().get(0), userWhoQueued);	
			}	
		}
	}

	@Override
	public void noMatches() {
		if(uri.startsWith("ytsearch: ")) {
			EmbedBuilder builder = new EmbedBuilder()
					.setDescription(mf.format(guild, "feedback.music.no-match"));	
			MusicUtil.sendEmbled(guild, builder);
		}else {
			MusicUtil.sendEmbledError(guild, mf.format(guild, "feedback.music.nothing-in-link"));
		}
	}

	@Override
	public void loadFailed(FriendlyException exception) {}
}
