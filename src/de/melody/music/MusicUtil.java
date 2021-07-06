package de.melody.music;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.melody.Melody;
import de.melody.utils.Emojis;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.sharding.ShardManager;


public class MusicUtil extends ListenerAdapter{

	private static ArrayList<String> verifiedurl = new ArrayList<String>();
	private Melody melody = Melody.INSTANCE;
	
	public static TextChannel getChannel(long guildid) {
		Guild guild;
		if((guild = Melody.INSTANCE.shardMan.getGuildById(guildid)) != null) {
			TextChannel channel;
			if((channel = guild.getTextChannelById(Melody.INSTANCE.entityManager.getGuildEntity(guildid).getChannelId())) != null) {
				return channel;
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static void sendEmbled(long guildid, EmbedBuilder builder) {		
		TextChannel channel;
		if((channel = getChannel(guildid)) != null) {
			builder.setColor(Melody.HEXEmbeld);
			channel.sendMessage(builder.build()).queue();
		}			
	}
	
	@SuppressWarnings("deprecation")
	public static void sendEmbledError(long guildid, String errormessage) {
		TextChannel channel;
		if((channel = getChannel(guildid)) != null) {
			EmbedBuilder builder = new EmbedBuilder();
			builder.setDescription(channel.getJDA().getEmoteById(Emojis.ANIMATED_TICK_RED).getAsMention()+" "+errormessage);
			builder.setColor(Melody.HEXEmbeldError);
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
			guild.getSelfMember().deafen(true).queue();
		}
	}
	
	@Override
	public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event) {
		Guild guild = event.getGuild();
		if(event.getMember() == guild.getSelfMember()) {
			if(!event.isDeafened()) {
				guild.getSelfMember().deafen(true).queue();
			}
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
		if(Melody.INSTANCE.entityManager.getGuildEntity(g.getIdLong()).is24_7() == false) {
			MusicController controller = Melody.INSTANCE.playerManager.getController(g.getIdLong());
			int time = controller.getAfkTime();
			if(time > 0) {
				time--;
				controller.setAfkTime(time);
				//ConsoleLogger.info(g.getIdLong()+"", time+"");
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
			AudioManager manager = vc.getGuild().getAudioManager();
			manager.closeAudioConnection();
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
		if(uri != null && verifiedurl.contains(uri)) {
			return true;
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
	
	public static void loadDomains() {
		verifiedurl.add("www.youtube.com");	
		verifiedurl.add("music.youtube.com");	
		verifiedurl.add("youtu.be");	
		verifiedurl.add("youtube.com");			
		verifiedurl.add("www.twitch.tv");	
		verifiedurl.add("vimeo.com");		
		verifiedurl.add("bandcamp.com");		
		verifiedurl.add("soundcloud.com");
	}
}
