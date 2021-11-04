package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.core.Melody;
import de.melody.utils.Utils;
import de.melody.utils.messenger.Messenger;
import de.melody.utils.messenger.Messenger.ErrorMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;


public class MusicUtil extends ListenerAdapter{


	private Melody melody = Melody.INSTANCE;
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel vc = event.getChannelLeft();
		Guild guild = event.getGuild();
		if(event.getMember() == guild.getSelfMember()) {	
			MusicKiller(guild);
		}else {
			if(vc.getMembers().contains(guild.getSelfMember()) && vc.getMembers().size() == 1) {
				melody.playerManager.getController(guild.getIdLong()).setAfkTime(240);
			}
		}
	}
	
	public static void sendEmbled(Guild guild, EmbedBuilder builder) {		
		TextChannel channel;
		if((channel = Melody.INSTANCE.getEntityManager().getGuildEntity(guild).getMusicChannel()) != null) {
			Messenger.sendMessageEmbed(channel, builder).queue();
		}			
	}
	
	public static void sendEmbled(Guild guild, String content) {		
		TextChannel channel;
		if((channel = Melody.INSTANCE.getEntityManager().getGuildEntity(guild).getMusicChannel()) != null) {
			Messenger.sendMessageEmbed(channel, content).queue();
		}			
	}
	
	public static void sendEmbledError(Guild guild, ErrorMessageBuilder builder) {
		TextChannel channel;
		if((channel = Melody.INSTANCE.getEntityManager().getGuildEntity(guild).getMusicChannel()) != null) {
			Messenger.sendErrorMessage(channel, builder);
		}				
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		Guild guild = event.getGuild();
		if(event.getMember() == guild.getSelfMember()) {
			AudioPlayer player = melody.playerManager.getController(guild.getIdLong()).getPlayer();
			player.setPaused(false);
		}
	}
	
	
	public static void onRefreshAutoDisabler(ShardManager shardMan) {
		try {
			for(Guild g :  shardMan.getGuilds()) {
				GuildVoiceState state;
				VoiceChannel vc;
				if((state = g.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
					AudioPlayer player = Melody.INSTANCE.playerManager.getController(g.getIdLong()).getPlayer();
					if(player.getPlayingTrack() != null) {
						if(vc.getMembers().size() == 1) {
							AFKManager(g);
						}
					}else {
						AFKManager(g);
					}
				}
			}
		}catch(IllegalStateException e) {}
	}
	
	public static void AFKManager(Guild g) {
		if(Melody.INSTANCE.entityManager.getGuildEntity(g).is24_7() == false) {
			MusicController controller = Melody.INSTANCE.playerManager.getController(g.getIdLong());
			int time = controller.getAfkTime();
			if(time > 0) {
				time--;
				controller.setAfkTime(time);
			}else {
				MusicKiller(g);
			}
		}
	}
	
	public static void MusicKiller(Guild g) {
		AudioPlayer player = Melody.INSTANCE.playerManager.getController(g.getIdLong()).getPlayer();
		GuildVoiceState state;
		VoiceChannel vc;
		
		player.stopTrack();
		if((state = g.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			vc.getGuild().getAudioManager().closeAudioConnection();
		}
		Melody.INSTANCE.playerManager.clearController(g.getIdLong());
	}
	
	public static String getTime(AudioTrackInfo trackinfo, Long time) {
		if(trackinfo != null) {
			long sekunden = trackinfo.length/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;	
			return trackinfo.isStream ? ":red_circle: STREAM" : (stunden > 0 ? stunden +"h " : "")+(minuten == 0 && stunden == 0? "" : minuten +"min ")+sekunden+"s";
		}else if(time >= 1){
			long sekunden = time/1000;
			long minuten = sekunden/60;
			long stunden = minuten/60;
			sekunden %= 60;
			minuten %= 60;	
			return (stunden > 0 ? stunden +"h " : "")+(minuten == 0 && stunden == 0? "" : minuten +"min ")+sekunden+"s";
		}
		return null;
	}
	
	public static String getTrackImageURL(String url) {
		String imageUrl = null;
		if(url.startsWith("https://www.youtube.com/watch?v=")) {
			String videoID = new String(url.replace("https://www.youtube.com/watch?v=", ""));
			imageUrl = "https://i.ytimg.com/vi_webp/"+videoID+"/maxresdefault.webp";
		}
		return imageUrl;
	}
	
	public static Long getTimeUntil(MusicController controller) {
		Queue queue = controller.getQueue();
		AudioPlayer player = controller.getPlayer();
		Long timeUntil = 0l;
		for(QueuedTrack track1 : queue.getQueuelist()) {
			timeUntil = timeUntil + track1.getTrack().getDuration();
		}
		timeUntil = timeUntil + player.getPlayingTrack().getDuration() - player.getPlayingTrack().getPosition();
		return timeUntil;
	}
	
	public static Service isUrlVerified(String url) {
		String domain = Utils.getDomain(url);
		Service service;
		if(domain != null) {
			if((service = Service.getFromDomain(domain)) != null) {
				return service;
			}
		}
		return null;
	}
}
