package de.melody.music;

import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.core.Constants;
import de.melody.core.Melody;
import de.melody.music.Queue.QueuedTrack;
import de.melody.utils.Emoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.sharding.ShardManager;


public class MusicUtil extends ListenerAdapter{

	private static final List<String> verifiedurl = List.of("youtube.com","youtu.be","soundcloud.com");		
	//verifiedurl.add("www.twitch.tv");	
	//verifiedurl.add("vimeo.com");		
	//verifiedurl.add("bandcamp.com");		
	private Melody melody = Melody.INSTANCE;
	
	public static TextChannel getChannel(Guild guild) {
		TextChannel channel;
		if((channel = guild.getTextChannelById(Melody.INSTANCE.entityManager.getGuildEntity(guild).getChannelId())) != null) {
			return channel;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static void sendEmbled(Guild guild, EmbedBuilder builder) {		
		TextChannel channel;
		if((channel = getChannel(guild)) != null) {
			builder.setColor(Constants.EMBEDCOLOR);
			channel.sendMessage(builder.build()).queue();
		}			
	}
	@SuppressWarnings("deprecation")
	public static void sendEmbledError(Guild guild, String errormessage) {
		TextChannel channel;
		if((channel = getChannel(guild)) != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(channel.getJDA().getEmoteById(Emoji.ANIMATED_TICK_RED).getAsMention()+" "+errormessage);
			builder.setColor(Constants.EMBELD_ERRORCOLOR);
			channel.sendMessage(builder.build()).queue();
		}				
	}
	
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
	
	public static Boolean isUrlVerified(String url) {
		String uri = getDomain(url);
		if(uri != null) {
			for(String vurl : verifiedurl) {
				if(uri.endsWith(vurl)) {
					return true;	
				}
			}
		}
		return false;
	}
	
	public static String getDomain(String url) {
		if(url.startsWith("http://") || url.startsWith("https://")) {
			String[] args = url.split("/");
			return args[2].toLowerCase();
		}
		return null;
	}
}
