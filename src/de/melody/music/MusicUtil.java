package de.melody.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.core.Melody;
import de.melody.tools.Utils;
import de.melody.tools.helper.MathHelper;
import de.melody.tools.messenger.Messenger;
import de.melody.tools.messenger.Messenger.ErrorMessageBuilder;
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
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
		VoiceChannel vc = event.getChannelLeft();
		Guild guild = event.getGuild();
		if(event.getMember() == guild.getSelfMember()) {	
			MusicKiller(guild);
			
		}else {
			if(vc.getMembers().contains(guild.getSelfMember()) && vc.getMembers().size() == 1) {
				Melody.INSTANCE._playerManager.getController(guild.getIdLong()).setAfkTime(240);
			}
		}
	}
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
		Guild guild = event.getGuild();
		if(event.getMember() == guild.getSelfMember()) {
			AudioPlayer player = Melody.INSTANCE._playerManager.getController(guild.getIdLong()).getPlayer();	
			player.setPaused(false);
		}
	}
	
	public static void sendEmbled(Guild guild, EmbedBuilder builder) {		
		TextChannel channel;
		if((channel = Melody.INSTANCE._playerManager.getAnounceChannel(guild)) != null) {
			Messenger.sendMessageEmbed(channel, builder).queue();
		}			
	}
	
	public static void sendEmbled(Guild guild, String content) {		
		TextChannel channel;
		if((channel = Melody.INSTANCE._playerManager.getAnounceChannel(guild)) != null) {
			Messenger.sendMessageEmbed(channel, content).queue();
		}			
	}
	
	public static void sendEmbledError(Guild guild, ErrorMessageBuilder builder) {
		TextChannel channel;
		if((channel = Melody.INSTANCE._playerManager.getAnounceChannel(guild)) != null) {
			Messenger.sendErrorMessage(channel, builder);
		}				
	}
	
	public static void onRefreshAutoDisabler(ShardManager shardMan) {
		try {
			for(Guild g :  shardMan.getGuilds()) {
				GuildVoiceState state;
				VoiceChannel vc;
				if((state = g.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
					AudioPlayer player = Melody.INSTANCE._playerManager.getController(g.getIdLong()).getPlayer();
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
		if(Melody.INSTANCE._entityManager.getGuildEntity(g).is24_7() == false) {
			MusicController controller = Melody.INSTANCE._playerManager.getController(g.getIdLong());
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
		AudioPlayer player = Melody.INSTANCE._playerManager.getController(g.getIdLong()).getPlayer();
		GuildVoiceState state;
		VoiceChannel vc;
		
		player.stopTrack();
		if((state = g.getSelfMember().getVoiceState()) != null && (vc = state.getChannel()) != null) {
			vc.getGuild().getAudioManager().closeAudioConnection();
		}
		Melody.INSTANCE._playerManager.clearController(g.getIdLong());
	}
	
	public static String getTime(AudioTrackInfo trackinfo, Long time) {
		if(trackinfo != null) {
			return trackinfo.isStream ? ":red_circle: STREAM" : MathHelper.decodeStringFromTimeMillis(trackinfo.length);
		}else if(time >= 1){
			return MathHelper.decodeStringFromTimeMillis(time);
		}
		return "NO DURATION FOUND";
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
	
	public static Service isDomainVerified(String url) {
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
